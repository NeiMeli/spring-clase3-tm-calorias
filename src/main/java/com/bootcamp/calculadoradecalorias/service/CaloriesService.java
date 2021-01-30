package com.bootcamp.calculadoradecalorias.service;

import com.bootcamp.calculadoradecalorias.dto.CaloriesByIngredientDTO;
import com.bootcamp.calculadoradecalorias.dto.IngredientDTO;

import java.util.List;

public interface CaloriesService {
    List<CaloriesByIngredientDTO> getCaloriesByIngredient(List<IngredientDTO> ingredients) throws CaloriesServiceException;
    CaloriesByIngredientDTO getIngredientWithMostCalories(List<CaloriesByIngredientDTO> caloriesByIngredient) throws CaloriesServiceException;
    double calculateTotalCalories(List<CaloriesByIngredientDTO> ingredients);
    double calculateCaloriesPerWeight(double calories, double weight);
}
