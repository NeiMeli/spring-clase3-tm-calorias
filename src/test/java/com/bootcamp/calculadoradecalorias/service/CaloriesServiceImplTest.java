package com.bootcamp.calculadoradecalorias.service;

import com.bootcamp.calculadoradecalorias.dto.CaloriesByIngredientDTO;
import com.bootcamp.calculadoradecalorias.dto.IngredientDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.bootcamp.calculadoradecalorias.common.TestConstants.CBI_LIST_SUPPLIER;
import static com.bootcamp.calculadoradecalorias.common.TestConstants.I_LIST_SUPPLIER;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CaloriesServiceImplTest {
    @Autowired
    CaloriesService service;

    @Test
    void testGetCaloriesByIngredient() throws CaloriesServiceException {
        List<IngredientDTO> ingredientDTOs = I_LIST_SUPPLIER.get();
        List<CaloriesByIngredientDTO> caloriesByIngredient = service.getCaloriesByIngredient(ingredientDTOs);
        assertEquals(9.9d, caloriesByIngredient.get(0).getCalories());
        assertEquals(197.4d, caloriesByIngredient.get(1).getCalories());
        assertEquals(77.3d, caloriesByIngredient.get(2).getCalories());
    }

    @Test
    void testGetIngredientWithMostCalories() throws CaloriesServiceException {
        List<CaloriesByIngredientDTO> caloriesByIngredientDTOs = CBI_LIST_SUPPLIER.get();
        assertEquals(caloriesByIngredientDTOs.get(2), service.getIngredientWithMostCalories(caloriesByIngredientDTOs));
    }

    @Test
    void testCalculateTotalCalories() {
        List<CaloriesByIngredientDTO> caloriesByIngredientDTOList = CBI_LIST_SUPPLIER.get();
        assertEquals(1228.5d, service.calculateTotalCalories(caloriesByIngredientDTOList));
    }

    @Test
    void testCalculateCaloriesPerWeight() {
        assertEquals(333.2d, service.calculateCaloriesPerWeight(9.8, 34));
        assertEquals(3196.8d, service.calculateCaloriesPerWeight(24, 133.2));
        assertEquals(300d, service.calculateCaloriesPerWeight(0.6, 500));
    }

    @Test
    void testValidateFails() {
        assertThatExceptionOfType(CaloriesServiceException.class)
                .isThrownBy(() -> service.getCaloriesByIngredient(new ArrayList<>()))
                .withMessageContaining(CaloriesServiceErrorImpl.EMPTY_LIST.getMessage());

        assertThatExceptionOfType(CaloriesServiceException.class)
                .isThrownBy(() -> service.getIngredientWithMostCalories(new ArrayList<>()))
                .withMessageContaining(CaloriesServiceErrorImpl.EMPTY_LIST.getMessage());

        assertThatExceptionOfType(CaloriesServiceException.class)
                .isThrownBy(() -> service.getCaloriesByIngredient(List.of(new IngredientDTO("i", -10))))
                .withMessageContaining(CaloriesServiceErrorImpl.INVALID_WEIGHT_VALUE.getMessage(-10));
        assertThatExceptionOfType(CaloriesServiceException.class)
                .isThrownBy(() -> service.getCaloriesByIngredient(List.of(new IngredientDTO("i", 0))))
                .withMessageContaining(CaloriesServiceErrorImpl.INVALID_WEIGHT_VALUE.getMessage(0));

        assertThatExceptionOfType(CaloriesServiceException.class)
                .isThrownBy(() -> service.getIngredientWithMostCalories(List.of(new CaloriesByIngredientDTO("i", -40))))
                .withMessageContaining(CaloriesServiceErrorImpl.INVALID_CALORIES_VALUE.getMessage(-40));
        assertThatExceptionOfType(CaloriesServiceException.class)
                .isThrownBy(() -> service.getIngredientWithMostCalories(List.of(new CaloriesByIngredientDTO("i", 0))))
                .withMessageContaining(CaloriesServiceErrorImpl.INVALID_CALORIES_VALUE.getMessage(0));
    }
}