package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateDataException extends ResponseStatusException {
    public DuplicateDataException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
