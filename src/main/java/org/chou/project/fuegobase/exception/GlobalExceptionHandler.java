package org.chou.project.fuegobase.exception;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalAccessError.class)
    @ResponseBody
    public ResponseEntity<?> handleIllegalAccessError(IllegalAccessError e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("access denied"));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllException(Exception e) {
        log.error("Global exception catch 500 error : " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
