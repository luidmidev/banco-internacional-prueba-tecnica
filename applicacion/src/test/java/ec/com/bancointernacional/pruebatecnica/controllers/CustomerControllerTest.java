package ec.com.bancointernacional.pruebatecnica.controllers;

import ec.com.bancointernacional.pruebatecnica.dto.CustomerResponse;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
                "spring.datasource.driverClassName=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.show-sql=true",
                "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect"
        }
)
@AutoConfigureTestRestTemplate
class CustomerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    // ---------------------------------------------------------
    //  TEST: GET /api/customers  (page)
    // ---------------------------------------------------------
    @Test
    void shouldReturnPagedCustomers() {

        ResponseEntity<PageResponse<CustomerResponse>> response = rest.exchange(
                url("/api/customers?page=0&size=10"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        var page = response.getBody();
        var pageDetails = page.getPage();

        assertThat(page.getContent()).isNotEmpty();
        assertThat(pageDetails.getSize()).isEqualTo(10);
        assertThat(pageDetails.getNumber()).isEqualTo(0);
    }

    // ---------------------------------------------------------
    //  TEST: GET /api/customers  con parámetro search
    // ---------------------------------------------------------
    @Test
    void shouldFilterCustomersUsingSearch() {

        ResponseEntity<PageResponse<CustomerResponse>> response = rest.exchange(
                url("/api/customers?search=Luis"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var page = response.getBody();
        assertThat(page).isNotNull();
        assertThat(page.getContent()).isNotEmpty();

        // Debe contener al menos un cliente llamado "Luis Vasquez"
        boolean foundLuis = page.getContent()
                .stream()
                .anyMatch(c -> c.getName().contains("Luis"));
        assertThat(foundLuis).isTrue();
    }

    // ---------------------------------------------------------
    //  TEST: GET /api/customers/identification/{id}
    // ---------------------------------------------------------
    @Test
    void shouldReturnCustomerByIdentificationNumber() {

        // Caso conocido mediante tu DataInitializer
        var cedula = "2300826357";

        ResponseEntity<CustomerResponse> response = rest.getForEntity(url("/api/customers/identification/" + cedula), CustomerResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        CustomerResponse customer = response.getBody();

        assertThat(customer).isNotNull();
        assertThat(customer.getIdentificationNumber()).isEqualTo(cedula);
        assertThat(customer.getName()).isEqualTo("Luis Vasquez");
    }

    // ---------------------------------------------------------
    //  TEST: GET /api/customers/identification/{id} NOT FOUND
    // ---------------------------------------------------------
    @Test
    void shouldReturnNotFoundForUnknownIdentificationNumber() {

        var invalidId = "0000000000";

        ResponseEntity<String> response = rest.getForEntity(url("/api/customers/identification/" + invalidId), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Cliente no encontrado");
    }

    // ---------------------------------------------------------
    // Soportar Page<T> con TestRestTemplate
    // ---------------------------------------------------------
    @Data
    static class PageResponse<T> {
        private List<T> content;
        private PageDetails page;

    }

    @Data
    static class PageDetails {
        private int number;
        private int size;
        private long totalPages;
        private long totalElements;
    }
}
