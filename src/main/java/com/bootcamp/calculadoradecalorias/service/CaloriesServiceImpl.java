package com.bootcamp.calculadoradecalorias.service;

import com.bootcamp.calculadoradecalorias.dao.CaloriesDAO;
import com.bootcamp.calculadoradecalorias.dto.CaloriesByIngredientDTO;
import com.bootcamp.calculadoradecalorias.dto.IngredientDTO;
import com.bootcamp.calculadoradecalorias.util.RoundUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.bootcamp.calculadoradecalorias.dao.CaloriesDAO.NOT_FOUND;

@Service
public class CaloriesServiceImpl implements CaloriesService {
    @Autowired
    CaloriesDAO dao;

    @Override
    public List<CaloriesByIngredientDTO> getCaloriesByIngredient(List<IngredientDTO> ingredients) throws CaloriesServiceException {
        validateList(ingredients, IngredientDTO::getWeight, CaloriesServiceErrorImpl.INVALID_WEIGHT_VALUE);
        List<CaloriesByIngredientDTO> caloriesByIngredientDTOS = new ArrayList<>();
        for (IngredientDTO ingredient : ingredients) {
            String ingredientName = ingredient.getName();
            double caloriesPerHundredGrams = dao.getCaloriesPerHundredGrams(ingredientName);
            if (caloriesPerHundredGrams == NOT_FOUND) throw new CaloriesServiceException(CaloriesServiceErrorImpl.INGREDIENT_NOT_FOUND);
            double caloriesPerGram = caloriesPerHundredGrams / 100; // me trae las calorías cada 100 gramos
            double totalCalories = calculateCaloriesPerWeight(caloriesPerGram, ingredient.getWeight());
            caloriesByIngredientDTOS.add(new CaloriesByIngredientDTO(ingredientName, totalCalories));
        }
        return caloriesByIngredientDTOS;
    }

    private <T> void validateList(List<T> list, Function<T, Double> doubleSupplier, CaloriesServiceErrorImpl error) throws CaloriesServiceException {
        if (list.isEmpty()) throw new CaloriesServiceException(CaloriesServiceErrorImpl.EMPTY_LIST);
        Optional<Double> invalidValue = list.stream().map(doubleSupplier).filter(w -> w <= 0).findFirst();
        if (invalidValue.isPresent()) {
            throw new CaloriesServiceException(error, invalidValue.get());
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public CaloriesByIngredientDTO getIngredientWithMostCalories(List<CaloriesByIngredientDTO> caloriesByIngredients) throws CaloriesServiceException {
        validateList(caloriesByIngredients, CaloriesByIngredientDTO::getCalories, CaloriesServiceErrorImpl.INVALID_CALORIES_VALUE);
        Comparator<CaloriesByIngredientDTO> comparator = (ci1, ci2) -> (int) (ci1.getCalories() - ci2.getCalories());
        return caloriesByIngredients.stream().max(comparator).get();
    }

    @Override
    public double calculateTotalCalories(List<CaloriesByIngredientDTO> ingredients) {
        return RoundUtil.roundOneDecimal(ingredients.stream().map(CaloriesByIngredientDTO::getCalories).reduce(0d, Double::sum));
    }

    @Override
    public double calculateCaloriesPerWeight(double calories, double weight) {
        return RoundUtil.roundOneDecimal(calories * weight);
    }
}
