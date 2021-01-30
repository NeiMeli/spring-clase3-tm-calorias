package com.bootcamp.calculadoradecalorias.service;

public class CaloriesServiceException extends Exception {
    public CaloriesServiceException(CaloriesServiceError error) {
        super(error.getMessage());
    }

    public CaloriesServiceException(CaloriesServiceError error, Object ... args) {
        super(error.getMessage(args));
    }
}
