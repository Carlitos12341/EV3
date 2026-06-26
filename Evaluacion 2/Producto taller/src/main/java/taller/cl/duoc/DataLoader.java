package taller.cl.duoc;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import taller.cl.duoc.model.Producto;
import taller.cl.duoc.repository.ProductoRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductoRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Faker faker = new Faker();

            for (int i = 0; i < 10; i++) {
                Producto p = new Producto();

                // Mapeo exacto basado en tus atributos de Producto
                p.setNombre("Repuesto " + faker.commerce().productName());
                p.setStock(faker.number().numberBetween(10, 50));
                p.setPrecioUnitario(faker.number().randomDouble(2, 5000, 85000));

                repository.save(p);
            }
            System.out.println(">>> BD Productos poblada con éxito.");
        }
    }
}