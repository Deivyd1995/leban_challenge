package com.challenge.leban.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.challenge.leban.entity.Departamento;

public interface IDepartamentoRepository extends JpaRepository<Departamento, String> {

    @Query("SELECT d FROM Departamento d WHERE " +
            "(:disponible IS NULL OR d.disponible = :disponible) AND " +
            "(:precioMin IS NULL OR d.precio >= :precioMin) AND " +
            "(:precioMax IS NULL OR d.precio <= :precioMax) AND " +
            "(:precioMin IS NULL OR :precioMax IS NULL OR :precioMin <= :precioMax)")
    List<Departamento> findWithFilters(
            @Param("disponible") Boolean disponible,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax);

}
