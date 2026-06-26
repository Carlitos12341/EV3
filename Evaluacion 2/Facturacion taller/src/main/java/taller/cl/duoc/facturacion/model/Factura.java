package taller.cl.duoc.facturacion.model;
import jakarta.persistence.*;

@Entity
public class Factura {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reparacionId;
    private Long clienteId;
    private Double montoTotal;
    private String estadoPago;
    // Generar Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReparacionId() {
        return reparacionId;
    }

    public void setReparacionId(Long reparacionId) {
        this.reparacionId = reparacionId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
}