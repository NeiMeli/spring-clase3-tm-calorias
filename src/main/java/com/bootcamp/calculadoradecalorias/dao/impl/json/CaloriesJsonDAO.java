package com.bootcamp.calculadoradecalorias.dao.impl.json;

import com.bootcamp.calculadoradecalorias.dao.CaloriesDAO;
import com.bootcamp.calculadoradecalorias.dto.CaloriesByIngredientDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

@Repository
public class CaloriesJsonDAO implements CaloriesDAO {
    private final List<CaloriesByIngredientDTO> database;

    public CaloriesJsonDAO () throws Exception {
        File file = ResourceUtils.getFile("src/main/resources/database/food.json");
        ObjectMapper objectMapper = new ObjectMapper();
        database = objectMapper.readValue(file, new TypeReference<>() {});
    }

    @Override
    public double getCaloriesPerHundredGrams(String ingredientName) {
        String nameToLowerCase = ingredientName.toLowerCase();
        return database.stream()
                .filter(i -> i.getName().toLowerCase().equals(nameToLowerCase))
                .map(CaloriesByIngredientDTO::getCalories)
                .findFirst()
                .orElse(NOT_FOUND);
    }
}
