package a2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Order must be 'processing' or 'pending'")
public class OrderStatusException extends RuntimeException {

}
