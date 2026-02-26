package com.challenge.leban.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.challenge.leban.dto.DepartamentoDto;
import com.challenge.leban.enums.Moneda;
import com.challenge.leban.util.IMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departamento")
@Getter
@Setter
@AllArgsConstructor
public class Departamento implements IMapper<DepartamentoDto> {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;
    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;
    @Column(name = "precio", nullable = false)
    private Float precio;
    @Enumerated(EnumType.STRING)
    private Moneda moneda;
    @Column(name = "metros_cuadrados")
    @JsonProperty("metros_cuadrados")
    private Float metros_cuadrados;
    @Column(name = "direccion", nullable = false, length = 500)
    private String direccion;
    @Column(name = "disponible", nullable = false)
    private boolean disponible;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    public Departamento() {
        // No-args constructor
    }

    @Override
    public DepartamentoDto getDTO() {
        return DepartamentoDto.builder()
                .id(id)
                .titulo(titulo)
                .descripcion(descripcion)
                .precio(precio)
                .moneda(moneda)
                .metros_cuadrados(metros_cuadrados)
                .direccion(direccion)
                .disponible(disponible)
                .build();
    }

    @Override
    public void setData(DepartamentoDto dto) {
        this.id = dto.getId();
        this.titulo = dto.getTitulo();
        this.descripcion = dto.getDescripcion();
        this.precio = dto.getPrecio();
        this.moneda = dto.getMoneda();
        this.metros_cuadrados = dto.getMetros_cuadrados();
        this.direccion = dto.getDireccion();
        this.disponible = dto.isDisponible();
    }

    @Override
    public String toString() {
        return "Inmueble [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", precio=" + precio
                + ", moneda=" + moneda + ", metros_cuadrados=" + metros_cuadrados + ", direccion=" + direccion
                + ", disponible=" + disponible + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}
