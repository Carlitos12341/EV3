package taller.cl.duoc.inventario.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productoId; // Referencia al microservicio Producto
    private Integer cantidadFisica;
    private Integer stockMinimo;
    private String pasilloUbicacion;

    //GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidadFisica() {
        return cantidadFisica;
    }

    public void setCantidadFisica(Integer cantidadFisica) {
        this.cantidadFisica = cantidadFisica;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getPasilloUbicacion() {
        return pasilloUbicacion;
    }

    public void setPasilloUbicacion(String pasilloUbicacion) {
        this.pasilloUbicacion = pasilloUbicacion;
    }
}