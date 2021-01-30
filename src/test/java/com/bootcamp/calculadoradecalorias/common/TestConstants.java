package com.bootcamp.calculadoradecalorias.common;

import com.bootcamp.calculadoradecalorias.dto.CaloriesByIngredientDTO;
import com.bootcamp.calculadoradecalorias.dto.IngredientDTO;
import com.bootcamp.calculadoradecalorias.dto.PlateDTO;
import net.minidev.json.JSONValue;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestConstants {
    public static final Supplier<IngredientDTO> I_DTO_1 = () -> new IngredientDTO("Acelgas", 30); // total kcal 9.9
    public static final Supplier<IngredientDTO> I_DTO_2 = () -> new IngredientDTO("Cebolla", 420); // total kcal 197.4
    public static final Supplier<IngredientDTO> I_DTO_3 = () -> new IngredientDTO("Mousse", 43.7); // total kcal 77.3
    public static final Supplier<List<IngredientDTO>> I_LIST_SUPPLIER = () -> List.of(I_DTO_1.get(), I_DTO_2.get(), I_DTO_3.get());

    public static final Supplier<CaloriesByIngredientDTO> CBI_DTO_1 = () -> new CaloriesByIngredientDTO("CBI-1", 400);
    public static final Supplier<CaloriesByIngredientDTO> CBI_DTO_2 = () -> new CaloriesByIngredientDTO("CBI-2", 230.5);
    public static final Supplier<CaloriesByIngredientDTO> CBI_DTO_3 = () -> new CaloriesByIngredientDTO("CBI-3", 598);
    public static final Supplier<List<CaloriesByIngredientDTO>> CBI_LIST_SUPPLIER = () -> List.of(CBI_DTO_1.get(), CBI_DTO_2.get(), CBI_DTO_3.get());
    public static final Function<Object, String> JSON_GENERATOR = JSONValue::toJSONString;

    public static final Supplier<PlateDTO> PLATE_DTO_1 = () -> new PlateDTO("Plate-1", I_LIST_SUPPLIER.get());
    public static final Supplier<PlateDTO> PLATE_DTO_2 = () -> new PlateDTO("Plate-2", List.of(I_DTO_1.get(), I_DTO_3.get()));

}
