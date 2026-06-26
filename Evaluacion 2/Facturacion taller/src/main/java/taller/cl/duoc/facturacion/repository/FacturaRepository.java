package taller.cl.duoc.facturacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taller.cl.duoc.facturacion.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
