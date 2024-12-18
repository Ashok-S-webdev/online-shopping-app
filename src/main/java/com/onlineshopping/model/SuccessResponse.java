package com.onlineshopping.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API Success JSON Response")
public class SuccessResponse {
    
    @Schema(description = "Status of JSON Response", example = "success")
    private String status;

    @Schema(description = "Message of JSON Response", example = "Action is done successfully")
    private String message;

    public SuccessResponse(String status, String message) {
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
