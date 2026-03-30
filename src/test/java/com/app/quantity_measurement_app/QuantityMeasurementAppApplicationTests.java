package com.app.quantity_measurement_app;

import com.app.quantity_measurement_app.dto.QuantityDTO;
import com.app.quantity_measurement_app.dto.QuantityInputDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
class QuantityMeasurementAppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = JsonMapper.builder().build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testCompareEqualQuantities() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("compare"))
                .andExpect(jsonPath("$.resultString").value("true"))
                .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    void testConvertQuantity() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(0.0, "INCHES", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("convert"))
                .andExpect(jsonPath("$.resultValue").value(12.0));
    }

    @Test
    void testAddQuantities() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.resultValue").value(2.0))
                .andExpect(jsonPath("$.resultUnit").value("FEET"))
                .andExpect(jsonPath("$.resultMeasurementType").value("LengthUnit"));
    }

    @Test
    void testSubtractQuantities() throws Exception {
        QuantityDTO q1 = new QuantityDTO(5.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(2.0, "FEET", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

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

        mockMvc.perform(post("/api/v1/quantities/divide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("divide"))
                .andExpect(jsonPath("$.resultValue").value(3.0));
    }

    @Test
    void testGetOperationHistory() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "KILOGRAM", "WeightUnit");
        QuantityDTO q2 = new QuantityDTO(1000.0, "GRAM", "WeightUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/quantities/history/operation/compare"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetMeasurementTypeHistory() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "LITRE", "VolumeUnit");
        QuantityDTO q2 = new QuantityDTO(1000.0, "MILLILITRE", "VolumeUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/quantities/history/type/VolumeUnit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetOperationCount() throws Exception {
        mockMvc.perform(get("/api/v1/quantities/count/compare"))
                .andExpect(status().isOk());
    }

    @Test
    void testErrorHandlingIncompatibleTypes() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(1.0, "KILOGRAM", "WeightUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testErrorHandlingInvalidUnit() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FOOT", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHE", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetErrorHistory() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(1.0, "KILOGRAM", "WeightUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/quantities/history/errored"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testWeightComparison() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "KILOGRAM", "WeightUnit");
        QuantityDTO q2 = new QuantityDTO(1000.0, "GRAM", "WeightUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultString").value("true"));
    }

    @Test
    void testVolumeAddition() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "LITRE", "VolumeUnit");
        QuantityDTO q2 = new QuantityDTO(1000.0, "MILLILITRE", "VolumeUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(2.0));
    }

    @Test
    void testTemperatureComparison() throws Exception {
        QuantityDTO q1 = new QuantityDTO(0.0, "CELSIUS", "TemperatureUnit");
        QuantityDTO q2 = new QuantityDTO(32.0, "FAHRENHEIT", "TemperatureUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultString").value("true"));
    }

    @Test
    void testDivisionByZeroReturns500() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(0.0, "INCHES", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/divide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testActuatorHealthEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void testActuatorMetricsEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isOk());
    }

    @Test
    void testContentNegotiationJson() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testJpaRepositoryPersistence() throws Exception {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/quantities/history/operation/compare"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)));
    }

    @Test
    void testMultipleOperationsHistory() throws Exception {
        QuantityDTO q1 = new QuantityDTO(2.0, "YARDS", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(6.0, "FEET", "LengthUnit");
        QuantityInputDTO input = new QuantityInputDTO(q1, q2);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/quantities/history/type/LengthUnit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(2)));
    }

    @Test
    void testAuthStatusEndpoint() throws Exception {
        mockMvc.perform(get("/auth/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").exists());
    }

    @Test
    void testAuthLoginEndpoint() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginUrl").value("/oauth2/authorization/google"));
    }
}