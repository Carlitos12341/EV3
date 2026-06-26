package taller.cl.duoc.inventario;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import taller.cl.duoc.inventario.model.Inventario;
import taller.cl.duoc.inventario.repository.InventarioRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private InventarioRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Faker faker = new Faker();

            for (int i = 0; i < 5; i++) {
                Inventario inv = new Inventario();

                // Mapeo exacto de los atributos de tu modelo Inventario
                inv.setProductoId(1L); // Relación genérica al primer producto
                inv.setCantidadFisica(faker.number().numberBetween(15, 100));
                inv.setStockMinimo(faker.number().numberBetween(5, 10));
                inv.setPasilloUbicacion("Pasillo " + faker.number().numberBetween(1, 10) + " - Estante " + faker.number().numberBetween(1, 5));

                repository.save(inv);
            }
            System.out.println(">>> BD Inventario poblada con éxito.");
        }
    }
}