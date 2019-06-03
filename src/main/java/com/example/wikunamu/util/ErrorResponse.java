package com.example.wikunamu.util;

import java.sql.Timestamp;

public class ErrorResponse {
    private Timestamp timestamp;
    private String message;
    private String error;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.setTimestamp(new Timestamp(System.currentTimeMillis()));
        this.setMessage(message);
    }

    public ErrorResponse(String message, String error) {
        this.setTimestamp(new Timestamp(System.currentTimeMillis()));
        this.setMessage(message);
        this.setError(error);
    }


    public Timestamp getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
