package a2.controller;

import a2.exception.EventsNotFoundException;
import a2.exception.NoItemsFoundOnOrderException;
import a2.exception.OrderStatusException;
import a2.exception.OrdersNotFoundException;
import com.itextpdf.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex, WebRequest req){
        String body = ex.toString();
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DocumentException.class)
    public String handleDocumentException(DocumentException de){
        return "There was an error processing the document";
    }

     @ResponseStatus(HttpStatus.NOT_FOUND)
     @ExceptionHandler(OrdersNotFoundException.class)
       public String handleOrdersNotFoundException(OrdersNotFoundException ex) {
        return "No orders were found";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoItemsFoundOnOrderException.class)
    public String handleNoItemsFoundOnOrderException(NoItemsFoundOnOrderException ex) {
        return "No items were found on order";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderStatusException.class)
    public String handleOrderStatusException(OrderStatusException ex) {
        return "Order must be of status 'pending' or 'processing'";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RemoteException.class)
    public String handleRemoteException(RemoteException ex) {
        return "Something went wrong!";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EventsNotFoundException.class)
    public String handleEventsNotFoundException(EventsNotFoundException ex) {
        return "No events were found";
    }
}
