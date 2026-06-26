package taller.cl.duoc;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import taller.cl.duoc.model.Reparacion;
import taller.cl.duoc.repository.ReparacionRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ReparacionRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Faker faker = new Faker();
            String[] detalles = {
                    "Cambio de pastillas de freno y rectificado de discos",
                    "Mantención de kilometraje, cambio de aceite y filtros",
                    "Alineación, balanceo y rotación de neumáticos",
                    "Reparación de sistema eléctrico y cambio de ampolletas"
            };

            for (int i = 0; i < 5; i++) {
                Reparacion r = new Reparacion();

                // Mapeo exacto basado en las propiedades de tu entidad Reparacion
                r.setCitaId((long) (i + 1));
                r.setMecanicoResponsable(faker.name().fullName());
                r.setDetalle(detalles[faker.number().numberBetween(0, 4)]);
                r.setCostoTotal(faker.number().randomDouble(2, 45000, 180000));

                repository.save(r);
            }
            System.out.println(">>> BD Reparaciones poblada con éxito.");
        }
    }
}