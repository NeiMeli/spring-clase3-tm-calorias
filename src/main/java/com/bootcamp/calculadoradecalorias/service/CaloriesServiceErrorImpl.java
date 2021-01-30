package com.bootcamp.calculadoradecalorias.service;

public enum CaloriesServiceErrorImpl implements CaloriesServiceError {
    INGREDIENT_NOT_FOUND("Ingrediente no encontrado"),
    EMPTY_LIST("Lista vacía"),
    INVALID_WEIGHT_VALUE("Valor de peso invalido: %s"),
    INVALID_CALORIES_VALUE("Valor de calorías inválido: %s");

    private final String message;

    CaloriesServiceErrorImpl(String s) {
        this.message = s;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessage(Object ... args) {
        return String.format(message, args);
    }
}
