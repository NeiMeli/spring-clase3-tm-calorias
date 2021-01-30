package com.bootcamp.calculadoradecalorias.controller;

import com.bootcamp.calculadoradecalorias.common.TestConstants;
import com.bootcamp.calculadoradecalorias.dto.CaloriesByIngredientDTO;
import com.bootcamp.calculadoradecalorias.dto.PlateDTO;
import com.bootcamp.calculadoradecalorias.dto.ResponseDTO;
import com.bootcamp.calculadoradecalorias.service.CaloriesService;
import com.bootcamp.calculadoradecalorias.service.CaloriesServiceErrorForTest;
import com.bootcamp.calculadoradecalorias.service.CaloriesServiceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.bootcamp.calculadoradecalorias.common.TestConstants.JSON_GENERATOR;
import static com.bootcamp.calculadoradecalorias.common.TestConstants.PLATE_DTO_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CaloriesControllerTest {
    public static final String MULTI_PATH = "/plates";
    public static final String SINGLE_PATH = "/plate";

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CaloriesService service;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testSingleHappy() throws Exception {
        CaloriesByIngredientDTO caloriesByIngredientDTO = new CaloriesByIngredientDTO("i", 100);
        List<CaloriesByIngredientDTO> caloriesByIngredient = List.of(caloriesByIngredientDTO);
        when(service.getCaloriesByIngredient(anyList()))
                .thenReturn(caloriesByIngredient);
        when(service.getIngredientWithMostCalories(anyList()))
                .thenReturn(caloriesByIngredientDTO);
        when(service.calculateTotalCalories(anyList()))
                .thenReturn(100d);

        MvcResult mvcResult = mockMvc.perform(post(SINGLE_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_GENERATOR.apply(PLATE_DTO_1.get())))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        ResponseDTO responseDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseDTO.class);
        assertEquals(100d, responseDTO.getTotalCalories());
        assertEquals(caloriesByIngredientDTO.getName(), responseDTO.getIngredientWithMostCalories());
        assertEquals(1, responseDTO.getCaloriesByIngredient().size());
        assertEquals(caloriesByIngredientDTO.getCalories(), responseDTO.getCaloriesByIngredient().get(0).getCalories());
        assertEquals(caloriesByIngredientDTO.getName(), responseDTO.getCaloriesByIngredient().get(0).getName());
    }

    @Test
    void testMultiHappy() throws Exception {
        CaloriesByIngredientDTO caloriesByIngredientDTO = new CaloriesByIngredientDTO("i", 100);
        List<CaloriesByIngredientDTO> caloriesByIngredient = List.of(caloriesByIngredientDTO);
        when(service.getCaloriesByIngredient(anyList()))
                .thenReturn(caloriesByIngredient);
        when(service.getIngredientWithMostCalories(anyList()))
                .thenReturn(caloriesByIngredientDTO);
        when(service.calculateTotalCalories(anyList()))
                .thenReturn(100d);

        PlateDTO plateDTO = PLATE_DTO_1.get();
        PlateDTO plateDTO2 = PLATE_DTO_1.get();
        List<PlateDTO> plates = List.of(plateDTO, plateDTO2);

        MvcResult mvcResult = mockMvc.perform(post(MULTI_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_GENERATOR.apply(plates)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        List<ResponseDTO> responses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

        responses.forEach(responseDTO -> {
            assertEquals(100d, responseDTO.getTotalCalories());
            assertEquals(caloriesByIngredientDTO.getName(), responseDTO.getIngredientWithMostCalories());
            assertEquals(1, responseDTO.getCaloriesByIngredient().size());
            assertEquals(caloriesByIngredientDTO.getCalories(), responseDTO.getCaloriesByIngredient().get(0).getCalories());
            assertEquals(caloriesByIngredientDTO.getName(), responseDTO.getCaloriesByIngredient().get(0).getName());
        });
    }

    @Test
    void testBadRequest() throws Exception {
        when(service.getCaloriesByIngredient(anyList()))
                .thenThrow(new CaloriesServiceException(CaloriesServiceErrorForTest.ERROR));
        // single
        MvcResult mvcResult = mockMvc.perform(post(SINGLE_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_GENERATOR.apply(TestConstants.PLATE_DTO_1.get())))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(CaloriesServiceErrorForTest.ERROR.getMessage());

        // multi
        MvcResult mvcResult2 = mockMvc.perform(post(MULTI_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_GENERATOR.apply(List.of(TestConstants.PLATE_DTO_1.get()))))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult2.getResolvedException())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(CaloriesServiceErrorForTest.ERROR.getMessage());
    }

    @Test
    void testInternalServerError() throws Exception {
        final String ERROR = "ERROR";
        when(service.getCaloriesByIngredient(anyList()))
                .thenThrow(new RuntimeException(ERROR));
        // single
        MvcResult mvcResult = mockMvc.perform(post(SINGLE_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_GENERATOR.apply(TestConstants.PLATE_DTO_1.get())))
                .andDo(print())
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(ERROR);

        // multi
        MvcResult mvcResult2 = mockMvc.perform(post(MULTI_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(JSON_GENERATOR.apply(List.of(TestConstants.PLATE_DTO_1.get()))))
                .andDo(print())
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andReturn();
        assertThat(mvcResult2.getResolvedException())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(ERROR);
    }
}