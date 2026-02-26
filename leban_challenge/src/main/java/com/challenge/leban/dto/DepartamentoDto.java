package com.challenge.leban.dto;

import com.challenge.leban.enums.Moneda;

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
    private String titulo;
    private String descripcion;
    private Float precio;
    private Moneda moneda;
    private Float metros_cuadrados;
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
