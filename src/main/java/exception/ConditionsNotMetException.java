package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConditionsNotMetException extends ResponseStatusException {
    public ConditionsNotMetException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
