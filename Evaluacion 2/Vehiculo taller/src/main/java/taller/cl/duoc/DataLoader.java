package taller.cl.duoc;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import taller.cl.duoc.model.Vehiculo;
import taller.cl.duoc.repository.VehiculoRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private VehiculoRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Faker faker = new Faker();
            String[] marcas = {"Toyota", "Hyundai", "Chevrolet", "Suzuki", "Nissan"};

            for (int i = 0; i < 5; i++) {
                Vehiculo v = new Vehiculo();

                // Mapeo exacto basado en las propiedades de tu entidad Vehiculo
                // Genera patentes con el formato chileno válido de 4 letras y 2 números
                v.setPatente(faker.regexify("[B-Z]{4}[0-9]{2}"));
                v.setMarca(marcas[faker.number().numberBetween(0, 5)]);
                v.setClienteId(1L); // Relación genérica al primer cliente

                repository.save(v);
            }
            System.out.println(">>> BD Vehículos poblada con éxito.");
        }
    }
}