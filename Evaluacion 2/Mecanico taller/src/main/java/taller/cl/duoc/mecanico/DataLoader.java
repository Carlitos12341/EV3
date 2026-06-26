package taller.cl.duoc.mecanico;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import taller.cl.duoc.mecanico.model.Mecanico;
import taller.cl.duoc.mecanico.repository.MecanicoRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MecanicoRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Faker faker = new Faker();

            for (int i = 0; i < 5; i++) {
                Mecanico m = new Mecanico();

                // Mapeo exacto basado en tu modelo Mecanico
                m.setNombreCompleto(faker.name().fullName());
                m.setDisponible(faker.bool().bool()); // Asigna true o false de forma aleatoria

                repository.save(m);
            }
            System.out.println(">>> BD Mecánicos poblada con éxito.");
        }
    }
}