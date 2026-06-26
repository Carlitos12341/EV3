package taller.cl.duoc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nombre del repuesto obligatorio")
    private String nombre;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @Positive(message = "El precio debe ser mayor a 0")
    private Double precioUnitario;

    //GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}