package ec.com.bancointernacional.pruebatecnica.services;

import com.peluware.omnisearch.OmniSearchOptions;
import com.peluware.omnisearch.jpa.JpaOmniSearch;
import com.peluware.springframework.web.problemdetails.ProblemDetails;
import ec.com.bancointernacional.pruebatecnica.dto.CustomerResponse;
import ec.com.bancointernacional.pruebatecnica.entities.Customer;
import ec.com.bancointernacional.pruebatecnica.mappers.CustomerMapper;
import ec.com.bancointernacional.pruebatecnica.utils.PeluwareDomainAdapters;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
public class CustomerService {

    private final EntityManager entityManager;
    private final JpaOmniSearch omniSearch;

    public CustomerService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.omniSearch = new JpaOmniSearch(entityManager);
    }

    /**
     * Realiza una búsqueda paginada de clientes utilizando OmniSearch.
     *
     * @param search   palabras clave para búsqueda general (text based search)
     * @param query    filtros específicos en formato rsql
     * @param pageable paginación y ordenamiento
     * @return Página de respuestas de clientes que coinciden con los criterios de búsqueda.
     */
    public Page<CustomerResponse> page(String search, String query, Pageable pageable) {

        log.debug("Buscando clientes con search='{}', query='{}', pageable={}", search, query, pageable);

        var page = PeluwareDomainAdapters.adaptToSpringPage(pageable, (pagination, sort) ->
                // La implementacion de jpa de omniSearch se encarga de construir la consulta dinamica mediante la api criteria de JPA
                omniSearch.page(Customer.class, new OmniSearchOptions()
                        .search(search)
                        .query(query)
                        .pagination(pagination)
                        .sort(sort)
                )
        );

        return page.map(CustomerMapper::toResponse);
    }

    public CustomerResponse getByIdentificationNumber(@NotNull String identificationNumber) {

        log.debug("Obteniendo cliente con número de identificación='{}'", identificationNumber);

        var customer = entityManager.createQuery("SELECT c FROM Customer c WHERE c.identificationNumber = :identificationNumber", Customer.class)
                .setParameter("identificationNumber", identificationNumber)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> ProblemDetails
                        .notFound("Cliente no encontrado.")
                        .extension("identificationNumber", identificationNumber)
                );

        return CustomerMapper.toResponse(customer);
    }

    /**
     * Obtiene el siguiente número de cliente desde la secuencia de base de datos.
     * Es thread safe y garantiza unicidad, es decir, es atómico.
     *
     * @return Siguiente número de cliente desde la secuencia de base de datos.
     */
    public long nextCustomerNumber() {
        log.debug("Obteniendo el siguiente número de cliente desde la secuencia de base de datos.");
        return ((Number) entityManager
                .createNativeQuery("SELECT nextval('customer_number_seq')")
                .getSingleResult())
                .longValue();
    }
}
