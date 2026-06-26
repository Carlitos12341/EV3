package taller.cl.duoc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Reparacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull private Long citaId;
    @NotBlank private String mecanicoResponsable;
    @NotBlank private String detalle;
    @Positive private Double costoTotal;

    //GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCitaId() {
        return citaId;
    }

    public void setCitaId(Long citaId) {
        this.citaId = citaId;
    }

    public String getMecanicoResponsable() {
        return mecanicoResponsable;
    }

    public void setMecanicoResponsable(String mecanicoResponsable) {
        this.mecanicoResponsable = mecanicoResponsable;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }
}