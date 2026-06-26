package taller.cl.duoc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long vehiculoId;
    private Long clienteId;
    private Long mecanicoId;
    private String fecha;
    private String motivo;

    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(Long vehiculoId) { this.vehiculoId = vehiculoId; }


    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }


    public Long getMecanicoId() { return mecanicoId; }
    public void setMecanicoId(Long mecanicoId) { this.mecanicoId = mecanicoId; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}