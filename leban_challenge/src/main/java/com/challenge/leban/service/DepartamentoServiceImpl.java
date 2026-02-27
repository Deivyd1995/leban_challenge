package com.challenge.leban.service;

import java.math.BigDecimal;
import java.util.List;

import com.challenge.leban.dto.DepartamentoDto;
import com.challenge.leban.entity.Departamento;
import com.challenge.leban.repository.IDepartamentoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DepartamentoServiceImpl implements IDepartamentoService {

    private IDepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(IDepartamentoRepository repository) {
        this.departamentoRepository = repository;
    }

    @Override
    public DepartamentoDto add(DepartamentoDto dto) {
        log.info("Adding departamento: {}", dto);
        Departamento departamento = new Departamento();
        departamento.setData(dto);
        departamentoRepository.save(departamento);
        return departamento.getDTO();
    }

    @Override
    public DepartamentoDto update(DepartamentoDto dto) {
        log.info("Updating departamento: {}", dto);
        Departamento departamento = departamentoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Departamento not found"));
        departamento.setData(dto);
        departamentoRepository.save(departamento);
        return departamento.getDTO();
    }

    @Override
    public List<DepartamentoDto> filterDepartamentos(String disponible, String precioMin, String precioMax) {

        Boolean disp = (disponible != null && !disponible.isEmpty()) ? Boolean.valueOf(disponible) : null;
        BigDecimal pMin = (precioMin != null && !precioMin.isEmpty()) ? BigDecimal.valueOf(Double.parseDouble(precioMin))  : null;
        BigDecimal pMax = (precioMax != null && !precioMax.isEmpty()) ? BigDecimal.valueOf(Double.parseDouble(precioMax)) : null;

        return departamentoRepository.findWithFilters(disp, pMin, pMax)
                .stream().map(Departamento::getDTO).toList();
    }

    @Override
    public List<DepartamentoDto> getAll() {
        return departamentoRepository.findAll().stream().map(Departamento::getDTO).toList();
    }
}
