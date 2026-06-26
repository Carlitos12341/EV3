package taller.cl.duoc.facturacion;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import taller.cl.duoc.facturacion.model.Factura;
import taller.cl.duoc.facturacion.repository.FacturaRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private FacturaRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Faker faker = new Faker();
            String[] estados = {"PAGADA", "PENDIENTE"};

            for (int i = 0; i < 5; i++) {
                Factura f = new Factura();
                f.setReparacionId(1L);
                f.setClienteId(1L);
                f.setMontoTotal(faker.number().randomDouble(2, 45000, 250000));
                f.setEstadoPago(estados[faker.number().numberBetween(0, 2)]);
                repository.save(f);
            }
            System.out.println(">>> BD Facturación poblada.");
        }
    }
}