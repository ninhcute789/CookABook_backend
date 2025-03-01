package NandK.CookABook.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import NandK.CookABook.entity.RestResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = { UsernameNotFoundException.class,
            BadCredentialsException.class })
    public ResponseEntity<RestResponse<Object>> handleIdExceptions(Exception exception) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(exception.getMessage());
        restResponse.setMessage("Xảy ra ngoại lệ...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse); // tra ve status 400 va message tu kia
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult(); // lay ra message loi
        final List<FieldError> fieldError = bindingResult.getFieldErrors();

        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(ex.getBody().getDetail());
        // lay message cua tung loi
        List<String> errors = fieldError.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        restResponse.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
}
