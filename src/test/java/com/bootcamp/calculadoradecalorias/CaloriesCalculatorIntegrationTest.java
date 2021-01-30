package com.bootcamp.calculadoradecalorias;

import com.bootcamp.calculadoradecalorias.dto.CaloriesByIngredientDTO;
import com.bootcamp.calculadoradecalorias.dto.IngredientDTO;
import com.bootcamp.calculadoradecalorias.dto.PlateDTO;
import com.bootcamp.calculadoradecalorias.dto.ResponseDTO;
import com.bootcamp.calculadoradecalorias.service.CaloriesService;
import com.bootcamp.calculadoradecalorias.service.CaloriesServiceErrorImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bootcamp.calculadoradecalorias.common.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CaloriesCalculatorIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CaloriesService service;

    @Autowired
    MockMvc mockMvc;

    public static final String MULTI_PATH = "/plates";
    public static final String SINGLE_PATH = "/plate";

    @Test
    void testSingleHappy() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(SINGLE_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_GENERATOR.apply(PLATE_DTO_1.get())))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        ResponseDTO responseDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseDTO.class);
        assertEquals(284.6d, responseDTO.getTotalCalories());
        assertEquals("Cebolla", responseDTO.getIngredientWithMostCalories());
        assertEquals(3, responseDTO.getCaloriesByIngredient().size());
        List<Double> calories = responseDTO.getCaloriesByIngredient().stream().map(CaloriesByIngredientDTO::getCalories).collect(Collectors.toList());
        Assertions.assertThat(calories).containsExactly(9.9d, 197.4d, 77.3d);
        List<String> names = responseDTO.getCaloriesByIngredient().stream().map(CaloriesByIngredientDTO::getName).collect(Collectors.toList());
        Assertions.assertThat(names).containsExactly("Acelgas", "Cebolla", "Mousse");
    }

    @Test
    void testMultiHappy() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(MULTI_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_GENERATOR.apply(List.of(PLATE_DTO_1.get(), PLATE_DTO_2.get()))))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        List<ResponseDTO> responses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(2, responses.size());

        ResponseDTO responseDTO = responses.get(0);
        assertEquals(284.6d, responseDTO.getTotalCalories());
        assertEquals("Cebolla", responseDTO.getIngredientWithMostCalories());
        assertEquals(3, responseDTO.getCaloriesByIngredient().size());
        List<Double> calories = responseDTO.getCaloriesByIngredient().stream().map(CaloriesByIngredientDTO::getCalories).collect(Collectors.toList());
        Assertions.assertThat(calories).containsExactly(9.9d, 197.4d, 77.3d);
        List<String> names = responseDTO.getCaloriesByIngredient().stream().map(CaloriesByIngredientDTO::getName).collect(Collectors.toList());
        Assertions.assertThat(names).containsExactly("Acelgas", "Cebolla", "Mousse");

        ResponseDTO responseDTO2 = responses.get(1);
        assertEquals(87.2d, responseDTO2.getTotalCalories());
        assertEquals("Mousse", responseDTO2.getIngredientWithMostCalories());
        assertEquals(2, responseDTO2.getCaloriesByIngredient().size());
        List<Double> calories2 = responseDTO2.getCaloriesByIngredient().stream().map(CaloriesByIngredientDTO::getCalories).collect(Collectors.toList());
        Assertions.assertThat(calories2).containsExactly(9.9d, 77.3d);
        List<String> names2 = responseDTO2.getCaloriesByIngredient().stream().map(CaloriesByIngredientDTO::getName).collect(Collectors.toList());
        Assertions.assertThat(names2).containsExactly("Acelgas", "Mousse");

    }

    @Test
    void testBadRequests() throws Exception {
        PlateDTOWithError plateWithoutIngredients = new PlateDTOWithError(new PlateDTO("p", new ArrayList<>()), CaloriesServiceErrorImpl.EMPTY_LIST.getMessage());
        PlateDTOWithError plateWithUnexistantIngredient = new PlateDTOWithError(new PlateDTO("p", List.of(new IngredientDTO("unknown", 40))), CaloriesServiceErrorImpl.INGREDIENT_NOT_FOUND.getMessage());
        PlateDTOWithError plateWithZeroWeightIngredient = new PlateDTOWithError(new PlateDTO("p", List.of(new IngredientDTO("Acelgas", 0))), CaloriesServiceErrorImpl.INVALID_WEIGHT_VALUE.getMessage(0));
        PlateDTOWithError plateWithNegativeWeightIngredient = new PlateDTOWithError(new PlateDTO("p", List.of(new IngredientDTO("Acelgas", -5))), CaloriesServiceErrorImpl.INVALID_WEIGHT_VALUE.getMessage(-5));
        List<PlateDTOWithError> invalidPlates = List.of(plateWithNegativeWeightIngredient, plateWithZeroWeightIngredient, plateWithUnexistantIngredient, plateWithoutIngredients);

        for (PlateDTOWithError plate : invalidPlates) {
            MvcResult mvcResult = mockMvc.perform(post(SINGLE_PATH)
                    .contentType(MediaType.APPLICATION_JSON).content(JSON_GENERATOR.apply(plate.plateDTO)))
                    .andDo(print())
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andReturn();
            assertThat(mvcResult.getResolvedException())
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining(plate.error);
        }
    }

    private static class PlateDTOWithError {
        private final PlateDTO plateDTO;
        private final String error;

        public PlateDTOWithError(PlateDTO plateDTO, String error) {
            this.plateDTO = plateDTO;
            this.error = error;
        }
    }
}
