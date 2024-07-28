package com.dac.dac.exption;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ErrorResponse<T> {

    private boolean success ;
    private String message;
    private T error;
    private LocalDateTime timestamp;
    private List<String> details;

    public ErrorResponse( String message, List<String> details,T error) {
            this.success = false;
            this.message = message;
            this.details = details;
            this.timestamp = LocalDateTime.now();
            this.error = error;

    }

}
