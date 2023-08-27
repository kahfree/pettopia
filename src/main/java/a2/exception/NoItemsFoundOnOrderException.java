package a2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No items were found on order")
public class NoItemsFoundOnOrderException extends RuntimeException {

}
