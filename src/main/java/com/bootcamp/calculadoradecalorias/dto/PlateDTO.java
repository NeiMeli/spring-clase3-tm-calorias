package com.bootcamp.calculadoradecalorias.dto;

import java.util.List;

public class PlateDTO {
    private String name;
    private List<IngredientDTO> ingredients;

    public PlateDTO(String name, List<IngredientDTO> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }
}
