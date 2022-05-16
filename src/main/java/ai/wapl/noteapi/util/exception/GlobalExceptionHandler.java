package ai.wapl.noteapi.util.exception;

import ai.wapl.noteapi.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static ai.wapl.noteapi.util.ResponseUtil.error;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ResponseEntity<ResponseUtil.ResponseDTO<?>> getResponseEntity(Exception e, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return error(headers, status, e.getMessage());
    }

    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class, MissingRequestValueException.class,
            MethodArgumentTypeMismatchException.class })
    public ResponseEntity<?> handleInvalidInput(Exception e) {
        return getResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(Exception e) {
        return getResponseEntity(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(Exception e) {
        logger.error("ResourceNotFoundException occurred: ", e.getMessage());
        return getResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceCallFailedException.class)
    public ResponseEntity<?> handleOtherAppError(Exception e) {
        return getResponseEntity(e, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler({ Exception.class, RuntimeException.class })
    public ResponseEntity<?> handleUnknown(Exception e) {
        logger.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return getResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
