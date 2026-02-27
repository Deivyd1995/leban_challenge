package com.challenge.leban.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.challenge.leban.dto.DepartamentoDto;
import com.challenge.leban.enums.Moneda;
import com.challenge.leban.repository.IDepartamentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class DepartamentoRESTControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IDepartamentoRepository departamentoRepository;

    @BeforeEach
    void setUp() {
        departamentoRepository.deleteAll();
    }

    @Test
    void getAllDepartamentos_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/departamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void addDepartamento_shouldCreateAndReturnDepartamento() throws Exception {
        DepartamentoDto dto = DepartamentoDto.builder()
                .titulo("Test Department")
                .descripcion("Test description for integration test")
                .precio(new BigDecimal("175000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(85f)
                .direccion("Test Street 789")
                .disponible(true)
                .build();

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.titulo", is("Test Department")))
                .andExpect(jsonPath("$.precio", is(175000)))
                .andExpect(jsonPath("$.moneda", is("ARS")))
                .andExpect(jsonPath("$.disponible", is(true)))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void addDepartamento_withValidationErrors_shouldReturnBadRequest() throws Exception {
        DepartamentoDto dto = DepartamentoDto.builder()
                .titulo("")
                .descripcion("")
                .precio(new BigDecimal("0"))
                .moneda(null)
                .metros_cuadrados(0f)
                .direccion("")
                .disponible(false)
                .build();

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDepartamento_shouldUpdateAndReturnUpdatedDepartamento() throws Exception {
        DepartamentoDto createDto = DepartamentoDto.builder()
                .titulo("Before Update")
                .descripcion("Before update description")
                .precio(new BigDecimal("90000"))
                .moneda(Moneda.USD)
                .metros_cuadrados(45f)
                .direccion("Before update address")
                .disponible(false)
                .build();

        String createResponse = mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = objectMapper.readTree(createResponse).get("id").asText();

        DepartamentoDto updateDto = DepartamentoDto.builder()
                .titulo("After Update")
                .descripcion("After update description")
                .precio(new BigDecimal("110000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(50f)
                .direccion("After update address")
                .disponible(true)
                .build();

        mockMvc.perform(put("/api/departamentos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is("After Update")))
                .andExpect(jsonPath("$.precio", is(110000)))
                .andExpect(jsonPath("$.moneda", is("ARS")))
                .andExpect(jsonPath("$.disponible", is(true)));
    }

    @Test
    void getAllDepartamentos_withDisponibleFilter_shouldReturnFilteredResults() throws Exception {
        DepartamentoDto availableDto = DepartamentoDto.builder()
                .titulo("Available Dept")
                .descripcion("Available department description")
                .precio(new BigDecimal("140000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(65f)
                .direccion("Available street")
                .disponible(true)
                .build();

        DepartamentoDto notAvailableDto = DepartamentoDto.builder()
                .titulo("Not Available Dept")
                .descripcion("Not available department description")
                .precio(new BigDecimal("160000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(70f)
                .direccion("Not available street")
                .disponible(false)
                .build();

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(availableDto)))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notAvailableDto)))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/departamentos?disponible=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("Available Dept")));
    }

    @Test
    void getAllDepartamentos_withPriceRangeFilter_shouldReturnFilteredResults() throws Exception {
        DepartamentoDto lowPriceDto = DepartamentoDto.builder()
                .titulo("Low Price")
                .descripcion("Low price department")
                .precio(new BigDecimal("70000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(55f)
                .direccion("Low price street")
                .disponible(true)
                .build();

        DepartamentoDto mediumPriceDto = DepartamentoDto.builder()
                .titulo("Medium Price")
                .descripcion("Medium price department")
                .precio(new BigDecimal("150000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(90f)
                .direccion("Medium price street")
                .disponible(true)
                .build();

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lowPriceDto)))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mediumPriceDto)))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/departamentos?precioMin=80000&precioMax=200000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("Medium Price")));
    }

    @Test
    void getAllDepartamentos_withMultipleFilters_shouldReturnFilteredResults() throws Exception {
        DepartamentoDto matchDto = DepartamentoDto.builder()
                .titulo("Perfect Match")
                .descripcion("Perfect match department")
                .precio(new BigDecimal("130000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(72f)
                .direccion("Perfect match street")
                .disponible(true)
                .build();

        DepartamentoDto noMatchDisponibleDto = DepartamentoDto.builder()
                .titulo("No Match Disponible")
                .descripcion("No match disponible department")
                .precio(new BigDecimal("135000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(75f)
                .direccion("No match disponible street")
                .disponible(false)
                .build();

        DepartamentoDto noMatchPriceDto = DepartamentoDto.builder()
                .titulo("No Match Price")
                .descripcion("No match price department")
                .precio(new BigDecimal("280000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(95f)
                .direccion("No match price street")
                .disponible(true)
                .build();

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchDto)))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noMatchDisponibleDto)))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noMatchPriceDto)))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/departamentos?disponible=true&precioMin=120000&precioMax=150000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("Perfect Match")));
    }
}