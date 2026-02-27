package com.challenge.leban.dto;

import java.math.BigDecimal;

import com.challenge.leban.enums.Moneda;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class DepartamentoDto {
    private String id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede exceder los 150 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotNull(message = "La moneda es obligatoria")
    private Moneda moneda;

    @NotNull(message = "Los metros cuadrados son obligatorios")
    @DecimalMin(value = "0.0", inclusive = false, message = "Los metros cuadrados deben ser mayores a 0")
    private Float metros_cuadrados;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 500, message = "La dirección no puede exceder los 500 caracteres")
    private String direccion;

    private boolean disponible;

    public DepartamentoDto() {
        // No-args constructor
    }

    @Override
    public String toString() {
        return "DepartamentoDto [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", precio="
                + precio
                + ", moneda=" + moneda + ", metros_cuadrados=" + metros_cuadrados + ", direccion=" + direccion
                + ", disponible=" + disponible + "]";
    }

}
