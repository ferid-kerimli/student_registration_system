package com.example.student_registration_system.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponse<T> {
    private T data;
    private int statusCode;
    private List<String> errors;
    private boolean isSuccess;

    public void success(T data, int statusCode) {
        this.data = data;
        this.statusCode = statusCode;
        this.errors = null;
        this.isSuccess = true;
    }

    public void failure(List<String> errors, int statusCode) {
        this.isSuccess = false;
        this.data = null;
        this.errors = errors;
        this.statusCode = statusCode;
    }

    public void failure(String error, int statusCode) {
        this.isSuccess = false;
        this.data = null;
        this.errors = List.of(error);
        this.statusCode = statusCode;
    }
}
