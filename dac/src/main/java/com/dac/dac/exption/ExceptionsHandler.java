package com.dac.dac.exption;


import com.fasterxml.jackson.databind.JsonMappingException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> recordNotFound(RecordNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getLocalizedMessage(), Collections.singletonList(e.getMessage()),null);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(DuplicatedEntryException.class)
    public ResponseEntity<ErrorResponse> duplicated(DuplicatedEntryException e) {
        ErrorResponse<Map<String,String>> errorResponse = new ErrorResponse(e.getLocalizedMessage(), Collections.singletonList(e.getMessage()),e.getDuplicateFields());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(EmptyListRecordException.class)
    public ResponseEntity<ErrorResponse> emptyList(EmptyListRecordException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getLocalizedMessage(), Arrays.asList(e.getMessage()),null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ErrorResponse> loginFaild(LoginFailedException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getLocalizedMessage(), Collections.singletonList(e.getMessage()),null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
   @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> sqlFaild(SQLIntegrityConstraintViolationException e){
        ErrorResponse errorResponse = new ErrorResponse("Please check the parameters", Collections.singletonList("Please check the parameters"),null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> inccorectInputValue(HttpMessageNotReadableException e){
        Throwable mostSpecificCause = e.getMostSpecificCause();

        if (mostSpecificCause instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) mostSpecificCause;
            for (JsonMappingException.Reference reference : jsonMappingException.getPath()) {
                String fieldName = reference.getFieldName();

                String message = String.format("Invalid value for field '%s'. Accepted values are: [S, L, M].", fieldName);
                ErrorResponse errorResponse = new ErrorResponse(message, Collections.singletonList(message),null);

                return ResponseEntity.badRequest().body(errorResponse);
            }
        }

        // For other cases of HttpMessageNotReadableException
        return ResponseEntity.badRequest().body(new ErrorResponse("Malformed JSON request.", Collections.singletonList("Malformed JSON request."),null));
    }


   /* @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> nullPointer(NullPointerException e){
        ErrorResponse errorResponse = new ErrorResponse("Please check parameters null", Collections.singletonList("please check parameters"),null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }*/
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> emptyList(TokenException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), Collections.singletonList(e.getMessage()),null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<ErrorResponse> nullPointer(RegisterException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), Collections.singletonList(e.getMessage()),null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

}
