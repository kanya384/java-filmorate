package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidParameterException extends ResponseStatusException {
    public InvalidParameterException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
