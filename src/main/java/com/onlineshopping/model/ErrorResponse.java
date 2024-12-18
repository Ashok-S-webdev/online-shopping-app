package com.onlineshopping.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API Error JSON Response")
public class ErrorResponse {
     
    @Schema(description = "Status of JSON Response", example = "error")
    private String status;

    @Schema(description = "Message of JSON Response", example = "Cannot complete Action")
    private String message;

    public ErrorResponse(String status, String message) {
        this.message = message;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
