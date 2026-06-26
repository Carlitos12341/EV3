package taller.cl.duoc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Vehiculo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Patente es obligatoria")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{4}|[A-Z]{4}[0-9]{2}$", message = "Formato de patente inválido (ej: AA1111 o AAAA11)")
    private String patente;

    @NotBlank(message = "Marca es obligatoria")
    private String marca;

    @NotNull(message = "Debe ingresar el ID del cliente dueño")
    private Long clienteId; // ID del microservicio Clientes

    //GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}