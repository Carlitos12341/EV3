package taller.cl.duoc.notificacion;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import taller.cl.duoc.notificacion.model.Notificacion;
import taller.cl.duoc.notificacion.repository.NotificacionRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private NotificacionRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Faker faker = new Faker();
            String[] tipos = {"EMAIL", "SMS", "WHATSAPP"};

            for (int i = 0; i < 5; i++) {
                Notificacion n = new Notificacion();

                // Seteando los campos reales de tu modelo Notificación
                n.setClienteId(1L);
                n.setTipo(tipos[faker.number().numberBetween(0, 3)]);
                n.setMensaje("Estimado cliente, su vehículo se encuentra listo para retiro.");
                n.setFechaEnvio("18-06-2026");
                n.setEstado("ENVIADO");

                repository.save(n);
            }
            System.out.println(">>> BD Notificaciones poblada con éxito.");
        }
    }
}