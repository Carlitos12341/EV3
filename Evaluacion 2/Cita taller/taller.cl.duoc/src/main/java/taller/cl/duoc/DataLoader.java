package taller.cl.duoc;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import taller.cl.duoc.model.Cita;
import taller.cl.duoc.repository.CitaRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CitaRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Faker faker = new Faker();

            for (int i = 0; i < 5; i++) {
                Cita c = new Cita();

                // Usando solo los atributos reales de tu modelo Cita
                c.setMotivo("Revisión de vehículo general");
                c.setFecha("18-06-2026");
                c.setMecanicoId(1L);
                c.setVehiculoId(1L);
                c.setClienteId(1L);

                repository.save(c);
            }
            System.out.println(">>> BD Citas poblada con éxito.");
        }
    }
}