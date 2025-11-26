package ec.com.bancointernacional.pruebatecnica.initializers;

import ec.com.bancointernacional.pruebatecnica.entities.Account;
import ec.com.bancointernacional.pruebatecnica.entities.Customer;
import ec.com.bancointernacional.pruebatecnica.schemas.AccountStatus;
import ec.com.bancointernacional.pruebatecnica.schemas.AccountType;
import ec.com.bancointernacional.pruebatecnica.schemas.IdentificationType;
import ec.com.bancointernacional.pruebatecnica.services.CustomerService;
import ec.com.bancointernacional.pruebatecnica.utils.EcuadorIdGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerDataInitializer implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    private final CustomerService service;

    @Override
    @Transactional
    public void run(String... args) {
        var count = (Long) entityManager.createQuery("SELECT COUNT(c) FROM Customer c").getSingleResult();

        if (count > 0) {
            log.info("Ya hay {} clientes en la base de datos, no se inicializan datos de prueba.", count);
            return; // ya hay datos, no inicializar
        }

        initFakerCustomers();
        initRealCustomers();
    }

    /**
     * Inicializa 20 clientes aleatorios usando la librería Faker.
     */
    private void initFakerCustomers() {
        var faker = new Faker(Locale.of("es", "EC"));
        for (int i = 0; i < 20; i++) {

            long nextCustomerNumber = service.nextCustomerNumber();

            var account = Account.builder()
                    .number(faker.number().digits(12))
                    .balance(BigDecimal.valueOf(faker.number().randomDouble(2, 100, 5000)))
                    .type(faker.options().option(AccountType.class))
                    .status(AccountStatus.ACTIVE)
                    .build();

            var customer = Customer.builder()
                    .name(faker.name().fullName())
                    .number(String.valueOf(nextCustomerNumber)) // se cifrará automáticamente si está configurado
                    .identificationType(IdentificationType.CI)
                    .identificationNumber(EcuadorIdGenerator.generateCedula())
                    .account(account)
                    .build();

            entityManager.persist(customer);
        }
    }

    /**
     * Inicializa 2 clientes con datos "reales" predefinidos
     */
    private void initRealCustomers() {
        var account1 = Account.builder()
                .number("123456789012")
                .balance(BigDecimal.valueOf(1500.75))
                .type(AccountType.SAVINGS)
                .status(AccountStatus.ACTIVE)
                .build();

        var customer1 = Customer.builder()
                .name("Luis Vasquez")
                .number(String.valueOf(service.nextCustomerNumber()))
                .identificationType(IdentificationType.CI)
                .identificationNumber("2300826357")
                .account(account1)
                .build();

        entityManager.persist(customer1);

        var account2 = Account.builder()
                .number("987654321098")
                .balance(BigDecimal.valueOf(3200.00))
                .type(AccountType.CHECKING)
                .status(AccountStatus.ACTIVE)
                .build();

        var customer2 = Customer.builder()
                .name("Maria Gomez")
                .number(String.valueOf(service.nextCustomerNumber()))
                .identificationType(IdentificationType.CI)
                .identificationNumber("2222222222")
                .account(account2)
                .build();

        entityManager.persist(customer2);
    }
}
