package com.challenge.leban.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.leban.dto.DepartamentoDto;
import com.challenge.leban.service.IDepartamentoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/departamentos")
@AllArgsConstructor
public class DepartamentoRESTController {

    private IDepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<List<DepartamentoDto>> getAllDepartamentos(
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(required = false) String precioMin,
            @RequestParam(required = false) String precioMax) {
        List<DepartamentoDto> departamentos = departamentoService.filterDepartamentos(disponible, precioMin, precioMax);
        return ResponseEntity.ok(departamentos);
    }

    @PostMapping
    public ResponseEntity<DepartamentoDto> addDepartamento(
            @Valid @RequestBody DepartamentoDto departamentoDto) {
        DepartamentoDto created = departamentoService.add(departamentoDto);
        return ResponseEntity.accepted().body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoDto> updateDepartamento(
            @PathVariable String id,
            @Valid @RequestBody DepartamentoDto departamentoDto) {
        try {
            DepartamentoDto updated = departamentoService.update(departamentoDto, id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
