package com.bootcamp.calculadoradecalorias.dao;

public interface CaloriesDAO {
    double NOT_FOUND = -1;
    double getCaloriesPerHundredGrams(String ingredientName);
}
