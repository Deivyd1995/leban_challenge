package com.challenge.leban.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.challenge.leban.entity.Departamento;

public class DepartamentoSpecifications {

    private static final String PRECIO_FIELD = "precio";

    private DepartamentoSpecifications() {
        // Utility class
    }

    public static Specification<Departamento> hasDisponible(Boolean disponible) {
        return (root, query, criteriaBuilder) -> {
            if (disponible == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("disponible"), disponible);
        };
    }

    public static Specification<Departamento> precioGreaterThanOrEqualTo(BigDecimal precioMin) {
        return (root, query, criteriaBuilder) -> {
            if (precioMin == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get(PRECIO_FIELD), precioMin);
        };
    }

    public static Specification<Departamento> precioLessThanOrEqualTo(BigDecimal precioMax) {
        return (root, query, criteriaBuilder) -> {
            if (precioMax == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get(PRECIO_FIELD), precioMax);
        };
    }

    public static Specification<Departamento> precioRange(BigDecimal precioMin, BigDecimal precioMax) {
        return (root, query, criteriaBuilder) -> {
            if (precioMin == null || precioMax == null) {
                return criteriaBuilder.conjunction();
            }
            if (precioMin.compareTo(precioMax) > 0) {
                return criteriaBuilder.disjunction();
            }
            return criteriaBuilder.between(root.get(PRECIO_FIELD), precioMin, precioMax);
        };
    }
}