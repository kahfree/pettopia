package a2.controller;

import a2.Helper.*;
import a2.exception.EventsNotFoundException;
import a2.exception.NoItemsFoundOnOrderException;
import a2.exception.OrderStatusException;
import a2.exception.OrdersNotFoundException;
import a2.model.*;
import a2.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@EnableScheduling
@EnableCaching
@RestController
public class PettopiaController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PetService petService;
    @Autowired
    private CheckupsService checkupsService;
    @Autowired
    private EventService eventService;
    @Autowired
    private Environment env;
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en","IE"));

    public PettopiaController() {
        System.out.println("Running Pettopia Controller");

    }

    @Cacheable(value = "customers")
    @GetMapping(value="/customers", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Customer>> getAllClients(@RequestParam(defaultValue="0") Integer page, @RequestParam(defaultValue="5") Integer size) {
        Page<Customer> cPage = customerService.findAllPageable(page,size);
        System.out.println("${cache.ttl}");
        cPage.getContent().forEach(c -> {
            Link selfLink = linkTo(methodOn(PettopiaController.class).getAllClients(page,size)).withSelfRel();
            c.add(selfLink);
            Link associatedOrdersLink = linkTo(methodOn(PettopiaController.class).getOrdersByCustomerID(c.getCustomerId())).withRel("Associated Orders");
            c.add(associatedOrdersLink);
        });

        HttpHeaders headers = new HttpHeaders();
        headers.add("Total-Page-Count", String.valueOf(cPage.getTotalPages()));
        headers.add("Elements-Per-Page",String.valueOf(cPage.getSize()));
        headers.add("Cache-TTL",env.getProperty("cache.ttl"));
        return ResponseEntity.ok()
                .headers(headers)
                .body(cPage.getContent());
    }

    @Cacheable(value = "customer")
    @GetMapping(value="/customers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Customer> getCustomerByID(@PathVariable int id) {
        System.out.println("Will only output first time");
        Optional<Customer> c = customerService.getCustomerByID(id);
        if(c.isPresent()){
            Link allCustomersLink = linkTo(methodOn(PettopiaController.class).getAllClients(0,5)).withRel("All Customers");
            c.get().add(allCustomersLink);
            return ResponseEntity.ok()
                    .header("Cache-TTL",env.getProperty("cache.ttl"))
                    .body(c.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CacheEvict(value = {"customer","customers","order"}, allEntries = true)
    //Use env ttl for cache, default to 10 seconds
    @Scheduled(fixedRateString = "${cache.ttl:10000}")
    public void emptyAllCache() {
        System.out.println("Clearing all cache");
    }
    @CacheEvict(value = {"customer","customers"}, allEntries = true)
    @PostMapping(value="/customers/add")
    public ResponseEntity<String> addCustomer(@Valid @RequestBody Customer newCustomer) {
        Customer c = customerService.addCustomer(newCustomer);
        return c != null ? new ResponseEntity<>("Customer has been added successfully",HttpStatus.CREATED) : new ResponseEntity<>("Ruh roh",HttpStatus.BAD_REQUEST);
    }

    @CacheEvict(value = {"customer","customers"}, allEntries = true)
    @PutMapping(value="/customers/edit", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> editCustomer(@RequestBody Customer newCustomer) {
        Customer c = customerService.addCustomer(newCustomer);
        return c != null ? new ResponseEntity<>("Customer has been edited successfully",HttpStatus.CREATED) : new ResponseEntity<>("Ruh roh",HttpStatus.BAD_REQUEST);
    }

    @CacheEvict(value = {"customer","customers"}, allEntries = true)
    @DeleteMapping(value="/customers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>("Customer has been deleted successfully",HttpStatus.OK);
    }

    @Cacheable(value = "order")
    @GetMapping(value="/orders/{customerID}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Orders>> getOrdersByCustomerID(@PathVariable int customerID) {
        List<Orders> olist = orderService.getOrdersByCustomerID(customerID);
        return ResponseEntity.ok()
                .header("Cache-TTL",env.getProperty("cache.ttl"))
                .body(olist);
    }

    @GetMapping(value="/orders/invoice/{orderID}")
    public ResponseEntity<InputStreamResource> getInvoiceByOrderID(@PathVariable int orderID) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //Check if order exists
        Optional<Orders> order = orderService.findOrder(orderID);
        if(order.isEmpty()){
            throw new OrdersNotFoundException();
        }
        //Check if order has items
        Orders orderForInvoice = order.get();
        if(orderForInvoice.getOrderItemCollection().size() == 0){
            throw new NoItemsFoundOnOrderException();
        }
        //Check if order is pending or processing
        if(!(orderForInvoice.getOrderStatusId().getStatus().equalsIgnoreCase("pending") || orderForInvoice.getOrderStatusId().getStatus().equalsIgnoreCase("processing"))){
            throw new OrderStatusException();
        }
        //Create Pdf
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfPTable table = new PdfPTable(4);
        Pdf.addTableHeader(table);
        Pdf.addOrderRow(table,orderForInvoice);
        //Calculate total + populate Pdf with order items
        double total = 0.0;
        for(OrderItem orderItem : orderForInvoice.getOrderItemCollection()){
            Pdf.addOrderItemRow(table,orderItem);
            total += (orderItem.getPrice().doubleValue() * orderItem.getQuantity());
        }
        Pdf.addTotalRow(table,currencyFormat.format(total));
        //Add table and close Pdf
        document.add(table);
        document.close();
        //Return Pdf
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
    }
    @GetMapping(value="/customers/invoice/{customerID}")
    public ResponseEntity<InputStreamResource> getInvoiceByCustomerID(@PathVariable int customerID) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //Get all valid customer orders
        List<Orders> ordersForInvoice = customerService.findOrdersForInvoiceByCustomerID(customerID)
                .stream()
                .filter(order -> order.getOrderStatusId().getStatus().equalsIgnoreCase("pending") || order.getOrderStatusId().getStatus().equalsIgnoreCase("processing"))
                        .toList();
        //Output all orders found for invoice to system
        ordersForInvoice.forEach(orders -> System.out.println(orders.getOrderStatusId().getStatus() + " - #" + orders.getOrderId() + " - (" + orders.getOrderItemCollection().size() + ")"));

        //Check if any orders were found
        if(ordersForInvoice.size() == 0){
            throw new OrdersNotFoundException();
        }
        //Create Pdf
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        //Format Pdf
        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(new float[]{ 60, 120,120,120 });
        table.setLockedWidth(true);
        double total = 0.0;
        Pdf.addTableHeader(table);
        //Add orders to Pdf
        for(Orders order : ordersForInvoice){
            if(order.getOrderItemCollection().size() > 0) {
            Pdf.addOrderRow(table,order);
            double subTotal = 0.0;
            for(OrderItem orderItem : order.getOrderItemCollection()){
                Pdf.addOrderItemRow(table,orderItem);
                subTotal += (orderItem.getPrice().doubleValue() * orderItem.getQuantity());
            }
            Pdf.addSubtotalRow(table,currencyFormat.format(subTotal));

            total += subTotal;
            }
        }
        Pdf.addTotalRow(table,currencyFormat.format(total));
        //Add table and close Pdf
        document.add(table);
        document.close();
        //Return Pdf
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
    }

    @Operation(summary = "Get a random dog photo")
    @GetMapping(value="/pets/doggo", produces = {MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> dogPhoto() throws IOException, InterruptedException {
        //Get dog photo from API
        var client = HttpClient.newHttpClient();
        String url = env.getProperty("api.dog.url");
        String key = env.getProperty("api.dog.key");
        var request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type","application/json")
                .header("x-api-key",key)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Check if response is valid
        if(response.statusCode() != 200){
            throw new RemoteException();
        }
        //Get image from response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());
        var imageRequest = HttpRequest.newBuilder(URI.create(rootNode.get(0).get("url").toString().replaceAll("\"","")))
                .GET()
                .build();
        HttpResponse<byte[]> imageResponse = client.send(imageRequest, HttpResponse.BodyHandlers.ofByteArray());
        //Return image
        return ResponseEntity.ok(imageResponse.body());
    }

    @Operation(summary = "Get 5 random news story about pets")
    @GetMapping(value="/pets/news", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Map<String,String>> newsStories() throws IOException, InterruptedException {
        //Get news stories from API
        var client = HttpClient.newHttpClient();
        int randomPage = new Random().nextInt(20);
        String base_url = env.getProperty("api.news.url");
        String key = env.getProperty("api.news.key");
        String url = base_url + "?q=pet&apiKey="+key+"&pageSize=5&page="+randomPage;
        var request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .build();
        //Check if response is valid
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
        if(response.statusCode() != 200){
            throw new RemoteException();
        }
        //Get news stories from response
        Map<String,String> newsMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode articlesNode = rootNode.get("articles");
        articlesNode.forEach(article -> newsMap.put(article.get("title").asText(),article.get("url").asText()));
        //Return news stories
        return ResponseEntity.ok(newsMap);
    }

    @Operation(summary = "Get a health summary of a Pet by ID")
    @GetMapping(value="/pets/health/{petID}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PetHealth> getPetHealthByPetID(@PathVariable int petID) {
        PetHealth petHealth = petService.getPetHealthByPetID(petID);
        return ResponseEntity.ok(petHealth);
    }

    @GetMapping(value="/pets/{petID}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Pet> getPetByID(@PathVariable int petID) {
        Optional<Pet> pet = petService.getPetByID(petID);
        return pet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all the pets the customer owns")
    @GetMapping(value="/customers/pets/{customerID}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Pet>> getPetByCustomerID(@PathVariable int customerID) {
        List<Pet> petList = petService.getPetsByCustomerID(customerID);
        return ResponseEntity.ok(petList);
    }

    @Operation(summary = "Check a pet in for their checkup")
    @PutMapping(value="/pets/checkups/checkin", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> checkinForCheckup(@RequestBody HashMap<String,String> checkups) {
        try {
            //Parse checkInTime
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime checkInTime = LocalDateTime.parse(checkups.get("checkInTime"),fmt);
            //Check pet in for checkup
            Checkups c = new Checkups(checkInTime,Integer.parseInt(checkups.get("pet")));
            Checkups checkup = checkupsService.addCheckup(c);
            //Return response
            return checkup != null ?
                    new ResponseEntity<>("Pet has been checked in for their checkup", HttpStatus.CREATED)
                    : new ResponseEntity<>("Ruh roh", HttpStatus.BAD_REQUEST);
        }catch (HttpMessageNotReadableException e){
            return ResponseEntity.badRequest().body("'checkInTime' must be of format 'yyyy-MM-dd HH:mm:ss");
        }
    }

    @Operation(summary = "Send an email reminder to each owner that has a pet due a checkup within the next week")
    @GetMapping(value="/pets/checkups/reminders", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> sendAllCheckupReminders(){
        List<Pet> petList = petService.getAllPets();
        //Gets a list of pets due a checkup in the next week
        List<Pet> petsDueCheckup = petList.stream().filter(pet -> {
            //Check if pet has a checkup
            LocalDate dateToCheck = pet.getPetHealth().getNextCheckUp().toLocalDate();
            return dateToCheck.isAfter(LocalDate.now()) && dateToCheck.isBefore(LocalDate.now().plusWeeks(4));
        }).toList();
        //Send email to each owner
        petsDueCheckup.forEach(pet -> {
            String body = "Hey "+pet.getCustomerId().getFirstName()+".\n";
            body += pet.getName() + " is due for a checkup in " + ChronoUnit.DAYS.between(LocalDate.now(),pet.getPetHealth().getNextCheckUp().toLocalDate()) + " day(s) on " + pet.getPetHealth().getNextCheckUp().toString();
            Email.sendEmail("ethancaff@gmail.com","Check up reminder",body);
        });
        //Return email addresses sent to
        List<String> emails = petsDueCheckup.stream().map(pet -> pet.getCustomerId().getEmail()).distinct().toList();
        return ResponseEntity.ok("Emails were sent to: " + emails);
    }


    @GetMapping(value="/events", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(defaultValue="0") Integer page, @RequestParam(defaultValue="5") Integer size) {
        Page<Event> eventPage = eventService.findAllPageable(page,size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Total-Page-Count", String.valueOf(eventPage.getTotalPages()));
        headers.add("Elements-Per-Page",String.valueOf(eventPage.getSize()));
        return ResponseEntity.ok()
                .headers(headers)
                .body(eventPage.getContent());
    }

    @PostMapping(value="/events/add/{customerID}")
    public ResponseEntity<String> addEvent(@RequestBody Event newEvent, @PathVariable int customerID) {
        Optional<Customer> cust = customerService.getCustomerByID(customerID);
        if(cust.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid customer ID");
        }
        newEvent.setCustomerId(cust.get());
        Event event = eventService.addEvent(newEvent);
        return event != null ?
                new ResponseEntity<>("Event has been added", HttpStatus.CREATED)
                : new ResponseEntity<>("There was an error adding the event", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value="/events/poster")
    public ResponseEntity<InputStreamResource> getPdfOfEvents() throws DocumentException, RemoteException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //Get all events
        List<Event> events = eventService.findAll();
        //Filter events to only those that are within the next 8 weeks
        List<Event> eventsOnSoon = events.stream().filter(event -> {
            Date dateToCheck = event.getDateOfEvent();
            LocalDate date = dateToCheck.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusWeeks(8));
        }).toList();
        //Throw exception if no events are found
        if(events.size() == 0){
            throw new EventsNotFoundException();
        }
        //Create and format Pdf
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfPTable table = new PdfPTable(3);
        Pdf.addEventHeader(table);
        eventsOnSoon.forEach(event -> Pdf.addEventRow(table,event));
        //Add table and close Pdf
        document.add(table);
        document.close();
        //Return Pdf
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
    }

    @Operation(summary = "Get a QR code that links to the events poster")
    @GetMapping(value="/events/code", produces={MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> generateQRCode() throws WriterException, IOException, DocumentException {
        //Create QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Link pdfLink = linkTo(methodOn(PettopiaController.class).getPdfOfEvents()).withRel("Events Poster");
        BitMatrix bitMatrix = qrCodeWriter.encode(pdfLink.getHref(), BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        //Convert QR code to png
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        //Return png
        return ResponseEntity.ok(pngData);
    }
}
