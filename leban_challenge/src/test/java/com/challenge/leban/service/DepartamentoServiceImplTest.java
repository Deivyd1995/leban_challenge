package com.challenge.leban.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.challenge.leban.dto.DepartamentoDto;
import com.challenge.leban.entity.Departamento;
import com.challenge.leban.enums.Moneda;
import com.challenge.leban.repository.IDepartamentoRepository;

@ExtendWith(MockitoExtension.class)
class DepartamentoServiceImplTest {

    @Mock
    private IDepartamentoRepository departamentoRepository;

    @InjectMocks
    private DepartamentoServiceImpl departamentoService;

    private DepartamentoDto departamentoDto;
    private Departamento departamento;

    @BeforeEach
    void setUp() {
        departamentoDto = DepartamentoDto.builder()
                .titulo("Departamento Centro")
                .descripcion("Hermoso departamento en el centro")
                .precio(BigDecimal.valueOf(150000))
                .moneda(Moneda.USD)
                .metros_cuadrados(75.5f)
                .direccion("Calle Principal 123")
                .disponible(true)
                .build();

        departamento = new Departamento();
        departamento.setId("test-id");
        departamento.setData(departamentoDto);
    }

    @Test
    void add_ShouldSaveAndReturnDto() {
        when(departamentoRepository.save(any(Departamento.class))).thenReturn(departamento);

        DepartamentoDto result = departamentoService.add(departamentoDto);

        assertNotNull(result);
        assertEquals(departamentoDto.getTitulo(), result.getTitulo());
        verify(departamentoRepository).save(any(Departamento.class));
    }

    @Test
    void update_ShouldUpdateAndReturnDto_WhenDepartamentoExists() {
        String id = "test-id";
        when(departamentoRepository.findById(id)).thenReturn(Optional.of(departamento));
        when(departamentoRepository.save(any(Departamento.class))).thenReturn(departamento);

        DepartamentoDto result = departamentoService.update(departamentoDto, id);

        assertNotNull(result);
        verify(departamentoRepository).findById(id);
        verify(departamentoRepository).save(departamento);
    }

    @Test
    void update_ShouldThrowRuntimeException_WhenDepartamentoNotFound() {
        String id = "non-existent-id";
        when(departamentoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> departamentoService.update(departamentoDto, id));
        verify(departamentoRepository).findById(id);
        verify(departamentoRepository, never()).save(any(Departamento.class));
    }

    @Test
    void filterDepartamentos_ShouldReturnFilteredList_WhenAllParametersProvided() {
        List<Departamento> departamentos = Arrays.asList(departamento);
        when(departamentoRepository.findAll(any(Specification.class))).thenReturn(departamentos);

        List<DepartamentoDto> result = departamentoService.filterDepartamentos(true, "100000", "200000");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(departamentoDto.getTitulo(), result.get(0).getTitulo());
        assertEquals(departamentoDto.getPrecio(), result.get(0).getPrecio());
        verify(departamentoRepository).findAll(any(Specification.class));
    }

    @Test
    void filterDepartamentos_ShouldReturnFilteredList_WhenSomeParametersNull() {
        List<Departamento> departamentos = Arrays.asList(departamento);
        when(departamentoRepository.findAll(any(Specification.class))).thenReturn(departamentos);

        List<DepartamentoDto> result = departamentoService.filterDepartamentos(null, "100000", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(departamentoDto.getTitulo(), result.get(0).getTitulo());
        assertEquals(departamentoDto.getPrecio(), result.get(0).getPrecio());
        verify(departamentoRepository).findAll(any(Specification.class));
    }

    @Test
    void filterDepartamentos_ShouldReturnFilteredList_WhenEmptyStringsProvided() {
        List<Departamento> departamentos = Arrays.asList(departamento);
        when(departamentoRepository.findAll(any(Specification.class))).thenReturn(departamentos);

        List<DepartamentoDto> result = departamentoService.filterDepartamentos(null, "", "");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(departamentoRepository).findAll(any(Specification.class));
    }

    @Test
    void filterDepartamentos_ShouldReturnEmptyList_WhenNoResults() {
        when(departamentoRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList());

        List<DepartamentoDto> result = departamentoService.filterDepartamentos(true, "100000", "200000");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(departamentoRepository).findAll(any(Specification.class));
    }

    @Test
    void getAll_ShouldReturnAllDepartamentos() {
        List<Departamento> departamentos = Arrays.asList(departamento);
        when(departamentoRepository.findAll()).thenReturn(departamentos);

        List<DepartamentoDto> result = departamentoService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(departamentoRepository).findAll();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoDepartamentos() {
        when(departamentoRepository.findAll()).thenReturn(Arrays.asList());

        List<DepartamentoDto> result = departamentoService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}