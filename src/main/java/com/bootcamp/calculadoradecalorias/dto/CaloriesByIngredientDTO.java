package com.bootcamp.calculadoradecalorias.dto;

public class CaloriesByIngredientDTO {
    private String name;
    private double calories;

    public CaloriesByIngredientDTO(String name, double calories) {
        this.name = name;
        this.calories = calories;
    }

    public CaloriesByIngredientDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
}
