package com.challenge.leban.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.leban.dto.DepartamentoDto;
import com.challenge.leban.service.IDepartamentoService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/departamentos")
@AllArgsConstructor
public class DepartamentoRESTController {

    private IDepartamentoService departamentoService;

    @GetMapping
    public List<DepartamentoDto> getAllDepartamentos(
            @RequestParam(required = false) String disponible,
            @RequestParam(required = false) String precioMin,
            @RequestParam(required = false) String precioMax) {
        return departamentoService.filterDepartamentos(disponible, precioMin, precioMax);
    }

    @PostMapping
    public DepartamentoDto addDepartamento(
            @RequestBody DepartamentoDto departamentoDto) {
        return departamentoService.add(departamentoDto);
    }

    @PutMapping("/{id}")
    public DepartamentoDto updateDepartamento(
            @PathVariable String id,
            @RequestBody DepartamentoDto departamentoDto) {
        return departamentoService.update(departamentoDto, id);
    }

}
