package com.bootcamp.calculadoradecalorias.dto;

import java.util.List;

public class ResponseDTO {
    private double totalCalories;
    private List<CaloriesByIngredientDTO> caloriesByIngredient;
    private String ingredientWithMostCalories;

    public ResponseDTO(double totalCalories, List<CaloriesByIngredientDTO> caloriesByIngredient, String ingredientWithMostCalories) {
        this.totalCalories = totalCalories;
        this.caloriesByIngredient = caloriesByIngredient;
        this.ingredientWithMostCalories = ingredientWithMostCalories;
    }

    public double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public List<CaloriesByIngredientDTO> getCaloriesByIngredient() {
        return caloriesByIngredient;
    }

    public void setCaloriesByIngredient(List<CaloriesByIngredientDTO> caloriesByIngredient) {
        this.caloriesByIngredient = caloriesByIngredient;
    }

    public String getIngredientWithMostCalories() {
        return ingredientWithMostCalories;
    }

    public void setIngredientWithMostCalories(String ingredientWithMostCalories) {
        this.ingredientWithMostCalories = ingredientWithMostCalories;
    }
}
