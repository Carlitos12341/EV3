package taller.cl.duoc;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import taller.cl.duoc.model.Cliente;
import taller.cl.duoc.repository.ClienteRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Faker faker = new Faker();
            for (int i = 0; i < 10; i++) {
                Cliente c = new Cliente();

                // Usando tus atributos reales del modelo
                c.setNombre(faker.name().fullName());
                c.setRut(faker.number().numberBetween(10000000, 25000000) + "-K");
                c.setEmail(faker.internet().emailAddress());

                repository.save(c);
            }
            System.out.println(">>> BD Clientes poblada.");
        }
    }
}