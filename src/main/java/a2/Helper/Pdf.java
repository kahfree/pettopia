package a2.Helper;

import a2.model.Event;
import a2.model.OrderItem;
import a2.model.Orders;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Stream;

public class Pdf {
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en","IE"));

    public static void addTableHeader(PdfPTable table) {
        Stream.of("Order Number","Product Name", "Quantity", "Price")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.ORANGE);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
    public static void addOrderRow(PdfPTable table, Orders order) {
        table.addCell(new PdfPCell(new Phrase("#"+order.getOrderId().toString())));
        table.addCell(new PdfPCell(new Phrase()));
        table.addCell(new PdfPCell(new Phrase()));
        table.addCell(new PdfPCell(new Phrase("No. Items "+order.getOrderItemCollection().size())));
    }
    public static void addSubtotalRow(PdfPTable table, String subtotal) {
        Stream.of("Subtotal:", "","", subtotal)
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
    public static void addTotalRow(PdfPTable table, String total) {
        Stream.of("Total:", "","", total)
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.DARK_GRAY);
                    Font font = new Font();
                    font.setColor(BaseColor.WHITE);
                    header.setPhrase(new Phrase(columnTitle,font));
                    table.addCell(header);
                });
    }
    public static void addOrderItemRow(PdfPTable table, OrderItem orderItem){
        table.addCell(new PdfPCell(new Phrase()));
        table.addCell(new PdfPCell(new Phrase(orderItem.getProductId().getName())));
        table.addCell(new PdfPCell(new Phrase(orderItem.getQuantity().toString())));
        table.addCell(new PdfPCell(new Phrase(currencyFormat.format(orderItem.getPrice()) + " ("+currencyFormat.format(orderItem.getPrice().doubleValue() * orderItem.getQuantity())+")")));
    }
    public static void addEventHeader(PdfPTable table) {
        Stream.of("Title","Date of Event", "Description")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.MAGENTA);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    public static void addEventRow(PdfPTable table, Event event) {
        table.addCell(new PdfPCell(new Phrase(event.getTitle())));
        table.addCell(new PdfPCell(new Phrase(event.getDateOfEvent().toString())));
        table.addCell(new PdfPCell(new Phrase(event.getDescription())));
    }
}
