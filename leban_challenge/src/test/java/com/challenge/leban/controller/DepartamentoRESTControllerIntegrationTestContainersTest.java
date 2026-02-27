package com.challenge.leban.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.challenge.leban.dto.DepartamentoDto;
import com.challenge.leban.enums.Moneda;
import com.challenge.leban.repository.IDepartamentoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class DepartamentoRESTControllerIntegrationTestContainersTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IDepartamentoRepository departamentoRepository;

    @BeforeEach
    void setUp() {
        departamentoRepository.deleteAll();
    }

    @Test
    void getAllDepartamentos_shouldReturnEmptyList() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/departamentos", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void addDepartamento_shouldCreateAndReturnDepartamento() {
        DepartamentoDto dto = DepartamentoDto.builder()
                .titulo("Departamento Centro")
                .descripcion("Hermoso departamento en el centro de la ciudad")
                .precio(new BigDecimal("150000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(75.5f)
                .direccion("Calle Principal 123")
                .disponible(true)
                .build();

        ResponseEntity<DepartamentoDto> response = restTemplate.postForEntity("/api/departamentos", dto, DepartamentoDto.class);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Departamento Centro", response.getBody().getTitulo());
        assertEquals(new BigDecimal("150000"), response.getBody().getPrecio());
        assertEquals(Moneda.ARS, response.getBody().getMoneda());
        assertTrue(response.getBody().isDisponible());
    }

    @Test
    void addDepartamento_withValidationErrors_shouldReturnBadRequest() {
        DepartamentoDto dto = DepartamentoDto.builder()
                .titulo("")
                .descripcion("")
                .precio(new BigDecimal("-100"))
                .moneda(Moneda.USD)
                .metros_cuadrados(-10f)
                .direccion("")
                .disponible(false)
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity("/api/departamentos", dto, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateDepartamento_shouldUpdateAndReturnDepartamento() {
        DepartamentoDto createDto = DepartamentoDto.builder()
                .titulo("Original")
                .descripcion("Descripción original")
                .precio(new BigDecimal("100000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(60f)
                .direccion("Dirección original")
                .disponible(true)
                .build();

        ResponseEntity<DepartamentoDto> createResponse = restTemplate.postForEntity("/api/departamentos", createDto, DepartamentoDto.class);
        String id = createResponse.getBody().getId();

        DepartamentoDto updateDto = DepartamentoDto.builder()
                .titulo("Actualizado")
                .descripcion("Descripción actualizada")
                .precio(new BigDecimal("200000"))
                .moneda(Moneda.USD)
                .metros_cuadrados(80f)
                .direccion("Dirección actualizada")
                .disponible(false)
                .build();

        HttpEntity<DepartamentoDto> requestEntity = new HttpEntity<>(updateDto);
        ResponseEntity<DepartamentoDto> response = restTemplate.exchange("/api/departamentos/" + id, HttpMethod.PUT, requestEntity, DepartamentoDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Actualizado", response.getBody().getTitulo());
        assertEquals(new BigDecimal("200000"), response.getBody().getPrecio());
        assertEquals(Moneda.USD, response.getBody().getMoneda());
        assertEquals(false, response.getBody().isDisponible());
    }

    @Test
    void getAllDepartamentos_withFilters_shouldReturnFilteredResults() {
        DepartamentoDto dto1 = DepartamentoDto.builder()
                .titulo("Depto 1")
                .descripcion("Descripción 1")
                .precio(new BigDecimal("100000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(50f)
                .direccion("Dirección 1")
                .disponible(true)
                .build();

        DepartamentoDto dto2 = DepartamentoDto.builder()
                .titulo("Depto 2")
                .descripcion("Descripción 2")
                .precio(new BigDecimal("200000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(70f)
                .direccion("Dirección 2")
                .disponible(false)
                .build();

        restTemplate.postForEntity("/api/departamentos", dto1, DepartamentoDto.class);
        restTemplate.postForEntity("/api/departamentos", dto2, DepartamentoDto.class);

        ResponseEntity<List> response = restTemplate.getForEntity("/api/departamentos?disponible=true", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllDepartamentos_withPriceRange_shouldReturnFilteredResults() {
        DepartamentoDto dto1 = DepartamentoDto.builder()
                .titulo("Barato")
                .descripcion("Descripción barata")
                .precio(new BigDecimal("50000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(40f)
                .direccion("Dirección barata")
                .disponible(true)
                .build();

        DepartamentoDto dto2 = DepartamentoDto.builder()
                .titulo("Medio")
                .descripcion("Descripción media")
                .precio(new BigDecimal("150000"))
                .moneda(Moneda.ARS)
                .metros_cuadrados(100f)
                .direccion("Dirección media")
                .disponible(true)
                .build();

        restTemplate.postForEntity("/api/departamentos", dto1, DepartamentoDto.class);
        restTemplate.postForEntity("/api/departamentos", dto2, DepartamentoDto.class);

        // Primero verificar que ambos departamentos se crearon
        ResponseEntity<List> allResponse = restTemplate.getForEntity("/api/departamentos", List.class);
        assertEquals(HttpStatus.OK, allResponse.getStatusCode());
        assertNotNull(allResponse.getBody());
        assertEquals(2, allResponse.getBody().size());

        // Ahora probar el filtro de precios
        ResponseEntity<List> response = restTemplate.getForEntity("/api/departamentos?precioMin=100000&precioMax=250000", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}