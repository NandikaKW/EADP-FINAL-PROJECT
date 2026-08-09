package com.ecom.product_api.adivisor;

import com.ecom.product_api.exception.EntryNotFoundException;
import com.ecom.product_api.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWiderExceptionHandler {
    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<StandardResponse> handleEntryNotFoundException(EntryNotFoundException entryNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new StandardResponse(
                        404,
                        "Entry Not Found",
                        entryNotFoundException.getMessage()
                ));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StandardResponse(
                        500,
                        "Internal Server Error",
                        ex.getMessage()
                ));
    }
}
