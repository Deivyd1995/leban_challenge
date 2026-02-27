package com.challenge.leban.service;

import java.math.BigDecimal;
import java.util.List;

import com.challenge.leban.exception.BusinessException;
import com.challenge.leban.exception.NotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.challenge.leban.dto.DepartamentoDto;
import com.challenge.leban.entity.Departamento;
import com.challenge.leban.repository.DepartamentoSpecifications;
import com.challenge.leban.repository.IDepartamentoRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DepartamentoServiceImpl implements IDepartamentoService {

    private IDepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(IDepartamentoRepository repository) {
        this.departamentoRepository = repository;
    }

    @Override
    public DepartamentoDto add(DepartamentoDto dto) {
        Departamento departamento = new Departamento();
        departamento.setData(dto);
        departamentoRepository.save(departamento);
        return departamento.getDTO();
    }

    @Override
    public DepartamentoDto update(DepartamentoDto dto, String id) {
                Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado"));
        departamento.setData(dto);
        departamentoRepository.save(departamento);
        return departamento.getDTO();
    }

    @Override
    public List<DepartamentoDto> filterDepartamentos(Boolean disponible, String precioMin, String precioMax) {
        BigDecimal pMin = (precioMin != null && !precioMin.isEmpty()) ? new BigDecimal(precioMin) : null;
        BigDecimal pMax = (precioMax != null && !precioMax.isEmpty()) ? new BigDecimal(precioMax) : null;

        if (pMin.doubleValue() > pMax.doubleValue() ) {
            throw new BusinessException("El precio minimo no puede ser mayor que precio maximo");
        }

        Specification<Departamento> spec = DepartamentoSpecifications.hasDisponible(disponible)
                .and(DepartamentoSpecifications.precioGreaterThanOrEqualTo(pMin))
                .and(DepartamentoSpecifications.precioLessThanOrEqualTo(pMax));

        return departamentoRepository.findAll(spec)
                .stream().map(Departamento::getDTO).toList();
    }

    @Override
    public List<DepartamentoDto> getAll() {
        return departamentoRepository.findAll().stream().map(Departamento::getDTO).toList();
    }
}
