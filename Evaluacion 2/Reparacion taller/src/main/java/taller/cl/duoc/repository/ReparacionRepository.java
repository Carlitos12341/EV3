package taller.cl.duoc.repository;

import taller.cl.duoc.model.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReparacionRepository extends JpaRepository<Reparacion, Long> {
}