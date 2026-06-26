package taller.cl.duoc.mecanico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taller.cl.duoc.mecanico.model.Mecanico;

@Repository
public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
}
