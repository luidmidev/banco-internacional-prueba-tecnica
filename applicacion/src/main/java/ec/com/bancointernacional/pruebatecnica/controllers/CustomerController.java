package ec.com.bancointernacional.pruebatecnica.controllers;

import ec.com.bancointernacional.pruebatecnica.dto.CustomerResponse;
import ec.com.bancointernacional.pruebatecnica.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> page(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String query,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.page(search, query, pageable));
    }

    /**
     * Obtiene un cliente por su número de identificación (cédula/pasaporte).
     */
    @GetMapping("/identification/{identificationNumber}")
    public ResponseEntity<CustomerResponse> getByIdentificationNumber(@PathVariable String identificationNumber) {
        return ResponseEntity.ok(service.getByIdentificationNumber(identificationNumber));
    }
}
