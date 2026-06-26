package taller.cl.duoc.proveedor;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import taller.cl.duoc.proveedor.model.Proveedor;
import taller.cl.duoc.proveedor.repository.ProveedorRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProveedorRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Faker faker = new Faker();

            for (int i = 0; i < 5; i++) {
                Proveedor p = new Proveedor();

                // Mapeo exacto basado en las propiedades de tu entidad Proveedor
                p.setRutEmpresa(faker.number().numberBetween(70000000, 99000000) + "-7");
                p.setRazonSocial(faker.company().name() + " SpA");
                p.setContactoNombre(faker.name().fullName());
                p.setEmail(faker.internet().emailAddress());
                p.setTelefono("+569" + faker.number().digits(8));

                repository.save(p);
            }
            System.out.println(">>> BD Proveedores poblada con éxito.");
        }
    }
}