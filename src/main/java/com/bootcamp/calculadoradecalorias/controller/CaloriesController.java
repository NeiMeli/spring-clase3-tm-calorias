package com.bootcamp.calculadoradecalorias.controller;

import com.bootcamp.calculadoradecalorias.dto.CaloriesByIngredientDTO;
import com.bootcamp.calculadoradecalorias.dto.IngredientDTO;
import com.bootcamp.calculadoradecalorias.dto.PlateDTO;
import com.bootcamp.calculadoradecalorias.dto.ResponseDTO;
import com.bootcamp.calculadoradecalorias.service.CaloriesService;
import com.bootcamp.calculadoradecalorias.service.CaloriesServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CaloriesController {
    @Autowired
    CaloriesService service;

    @PostMapping("/plate")
    @ResponseBody
    public ResponseEntity<ResponseDTO> calculatePlateData(@RequestBody PlateDTO plate) {
        try {
            ResponseDTO responseDTO = calculatePlateData(plate.getIngredients());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (final Exception e) {
            HttpStatus status;
            if (e instanceof CaloriesServiceException) {
                status = HttpStatus.BAD_REQUEST;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    @PostMapping("/plates")
    @ResponseBody
    public ResponseEntity<List<ResponseDTO>> calculatePlateDataMulti(@RequestBody List<PlateDTO> plates) {
        try {
            final List<ResponseDTO> responses = new ArrayList<>();
            List<List<IngredientDTO>> ingredientsByPlate = plates.stream().map(PlateDTO::getIngredients).collect(Collectors.toList());
            for (List<IngredientDTO> ingredients : ingredientsByPlate) {
                responses.add(calculatePlateData(ingredients));
            }
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (final Exception e) {
            HttpStatus status;
            if (e instanceof CaloriesServiceException) {
                status = HttpStatus.BAD_REQUEST;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    private ResponseDTO calculatePlateData(List<IngredientDTO> ingredients) throws CaloriesServiceException {
        List<CaloriesByIngredientDTO> caloriesByIngredient = service.getCaloriesByIngredient(ingredients);
        CaloriesByIngredientDTO ingredientWithMostCalories = service.getIngredientWithMostCalories(caloriesByIngredient);
        double totalCalories = service.calculateTotalCalories(caloriesByIngredient);
        return new ResponseDTO(totalCalories, caloriesByIngredient, ingredientWithMostCalories.getName());
    }
}
