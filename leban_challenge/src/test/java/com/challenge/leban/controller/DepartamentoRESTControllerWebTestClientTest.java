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
class DepartamentoRESTControllerWebTestClientTest {

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
    void addDepartamento_shouldCreateDepartamento() throws Exception {
        DepartamentoDto dto = DepartamentoDto.builder()
                .titulo("Depto Test")
                .descripcion("Descripción de test")
                .precio(new BigDecimal("120000"))
                .moneda(Moneda.USD)
                .metros_cuadrados(65f)
                .direccion("Calle Test 456")
                .disponible(true)
                .build();

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.titulo", is("Depto Test")))
                .andExpect(jsonPath("$.precio", is(120000)))
                .andExpect(jsonPath("$.moneda", is("USD")))
                .andExpect(jsonPath("$.disponible", is(true)))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void addDepartamento_withInvalidData_shouldReturnBadRequest() throws Exception {
        DepartamentoDto dto = DepartamentoDto.builder()
                .titulo("")
                .descripcion("")
                .precio(new BigDecimal("-50000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(-20f)
                .direccion("")
                .disponible(false)
                .build();

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDepartamento_shouldUpdateExistingDepartamento() throws Exception {
        DepartamentoDto createDto = DepartamentoDto.builder()
                .titulo("Original Title")
                .descripcion("Original description")
                .precio(new BigDecimal("80000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(55f)
                .direccion("Original address")
                .disponible(true)
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
                .titulo("Updated Title")
                .descripcion("Updated description")
                .precio(new BigDecimal("95000"))
                .moneda(Moneda.USD)
                .metros_cuadrados(60f)
                .direccion("Updated address")
                .disponible(false)
                .build();

        mockMvc.perform(put("/api/departamentos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is("Updated Title")))
                .andExpect(jsonPath("$.precio", is(95000)))
                .andExpect(jsonPath("$.moneda", is("USD")))
                .andExpect(jsonPath("$.disponible", is(false)));
    }

    @Test
    void getAllDepartamentos_withDisponibleFilter_shouldReturnFilteredResults() throws Exception {
        DepartamentoDto availableDto = DepartamentoDto.builder()
                .titulo("Available")
                .descripcion("Available description")
                .precio(new BigDecimal("100000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(70f)
                .direccion("Available address")
                .disponible(true)
                .build();

        DepartamentoDto notAvailableDto = DepartamentoDto.builder()
                .titulo("Not Available")
                .descripcion("Not available description")
                .precio(new BigDecimal("150000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(80f)
                .direccion("Not available address")
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
                .andExpect(jsonPath("$[0].titulo", is("Available")));
    }

    @Test
    void getAllDepartamentos_withPriceFilters_shouldReturnFilteredResults() throws Exception {
        DepartamentoDto cheapDto = DepartamentoDto.builder()
                .titulo("Cheap")
                .descripcion("Cheap description")
                .precio(new BigDecimal("60000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(50f)
                .direccion("Cheap address")
                .disponible(true)
                .build();

        DepartamentoDto expensiveDto = DepartamentoDto.builder()
                .titulo("Expensive")
                .descripcion("Expensive description")
                .precio(new BigDecimal("180000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(90f)
                .direccion("Expensive address")
                .disponible(true)
                .build();

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cheapDto)))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expensiveDto)))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/departamentos?precioMin=70000&precioMax=200000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("Expensive")));
    }

    @Test
    void getAllDepartamentos_withAllFilters_shouldReturnFilteredResults() throws Exception {
        DepartamentoDto dto1 = DepartamentoDto.builder()
                .titulo("Match")
                .descripcion("Match description")
                .precio(new BigDecimal("120000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(75f)
                .direccion("Match address")
                .disponible(true)
                .build();

        DepartamentoDto dto2 = DepartamentoDto.builder()
                .titulo("No Match Available")
                .descripcion("No match available description")
                .precio(new BigDecimal("130000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(80f)
                .direccion("No match available address")
                .disponible(false)
                .build();

        DepartamentoDto dto3 = DepartamentoDto.builder()
                .titulo("No Match Price")
                .descripcion("No match price description")
                .precio(new BigDecimal("300000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(85f)
                .direccion("No match price address")
                .disponible(true)
                .build();

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto3)))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/departamentos?disponible=true&precioMin=100000&precioMax=200000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("Match")));
    }
}