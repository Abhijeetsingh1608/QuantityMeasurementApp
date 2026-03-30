package com.app.quantity_measurement_app.controller;
import com.app.quantity_measurement_app.dto.QuantityDTO;
import com.app.quantity_measurement_app.dto.QuantityInputDTO;
import com.app.quantity_measurement_app.dto.QuantityMeasurementDTO;
import com.app.quantity_measurement_app.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private IQuantityMeasurementService service;

    @BeforeEach
    void setUp() {
        objectMapper = JsonMapper.builder().build();
    }

    @Test
    void testCompareQuantities() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setThisValue(1.0);
        result.setThisUnit("FEET");
        result.setThisMeasurementType("LengthUnit");
        result.setThatValue(12.0);
        result.setThatUnit("INCHES");
        result.setThatMeasurementType("LengthUnit");
        result.setOperation("compare");
        result.setResultString("true");
        result.setError(false);

        Mockito.when(service.compareQuantities(any(QuantityDTO.class), any(QuantityDTO.class), any()))
                .thenReturn(result);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("compare"))
                .andExpect(jsonPath("$.resultString").value("true"))
                .andExpect(jsonPath("$.error").value(false));

        Mockito.verify(service).compareQuantities(any(QuantityDTO.class), any(QuantityDTO.class), any());
    }

    @Test
    void testAddQuantities() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setThisValue(1.0);
        result.setThisUnit("FEET");
        result.setThisMeasurementType("LengthUnit");
        result.setThatValue(12.0);
        result.setThatUnit("INCHES");
        result.setThatMeasurementType("LengthUnit");
        result.setOperation("add");
        result.setResultValue(2.0);
        result.setResultUnit("FEET");
        result.setResultMeasurementType("LengthUnit");
        result.setError(false);

        Mockito.when(service.addQuantities(any(QuantityDTO.class), any(QuantityDTO.class), any()))
                .thenReturn(result);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.resultValue").value(2.0))
                .andExpect(jsonPath("$.resultUnit").value("FEET"));
    }

    @Test
    void testConvertQuantity() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(0.0, "INCHES", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setThisValue(1.0);
        result.setThisUnit("FEET");
        result.setThisMeasurementType("LengthUnit");
        result.setThatValue(0.0);
        result.setThatUnit("INCHES");
        result.setThatMeasurementType("LengthUnit");
        result.setOperation("convert");
        result.setResultValue(12.0);
        result.setError(false);

        Mockito.when(service.convertQuantity(any(QuantityDTO.class), any(QuantityDTO.class), any()))
                .thenReturn(result);

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("convert"))
                .andExpect(jsonPath("$.resultValue").value(12.0));
    }

    @Test
    void testSubtractQuantities() throws Exception {
        QuantityDTO q1 = new QuantityDTO(5.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(2.0, "FEET", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setOperation("subtract");
        result.setResultValue(3.0);
        result.setError(false);

        Mockito.when(service.subtractQuantities(any(QuantityDTO.class), any(QuantityDTO.class), any()))
                .thenReturn(result);

        mockMvc.perform(post("/api/v1/quantities/subtract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("subtract"))
                .andExpect(jsonPath("$.resultValue").value(3.0));
    }

    @Test
    void testDivideQuantities() throws Exception {
        QuantityDTO q1 = new QuantityDTO(6.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(2.0, "FEET", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setOperation("divide");
        result.setResultValue(3.0);
        result.setError(false);

        Mockito.when(service.divideQuantities(any(QuantityDTO.class), any(QuantityDTO.class), any()))
                .thenReturn(result);

        mockMvc.perform(post("/api/v1/quantities/divide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("divide"))
                .andExpect(jsonPath("$.resultValue").value(3.0));
    }

    @Test
    void testGetOperationHistory() throws Exception {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setOperation("compare");
        dto.setResultString("true");

        Mockito.when(service.getHistoryByOperation("compare")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/quantities/history/operation/compare"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operation").value("compare"));
    }

    @Test
    void testGetMeasurementTypeHistory() throws Exception {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisMeasurementType("LengthUnit");
        dto.setOperation("add");

        Mockito.when(service.getHistoryByMeasurementType("LengthUnit")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/quantities/history/type/LengthUnit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].thisMeasurementType").value("LengthUnit"));
    }

    @Test
    void testGetOperationCount() throws Exception {
        Mockito.when(service.getCountByOperation("COMPARE")).thenReturn(5L);

        mockMvc.perform(get("/api/v1/quantities/count/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));
    }

    @Test
    void testGetErrorHistory() throws Exception {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setError(true);
        dto.setErrorMessage("Test error");

        Mockito.when(service.getErrorHistory()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/quantities/history/errored"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].error").value(true))
                .andExpect(jsonPath("$[0].errorMessage").value("Test error"));
    }
}