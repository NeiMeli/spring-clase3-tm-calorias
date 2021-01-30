package com.bootcamp.calculadoradecalorias.service;

public enum CaloriesServiceErrorForTest implements CaloriesServiceError {
    ERROR;

    @Override
    public String getMessage() {
        return "Error";
    }

    @Override
    public String getMessage(Object... args) {
        return String.format("Error %s", args);
    }
}
