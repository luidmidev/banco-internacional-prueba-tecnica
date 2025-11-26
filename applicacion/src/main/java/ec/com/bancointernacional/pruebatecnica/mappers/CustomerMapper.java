package ec.com.bancointernacional.pruebatecnica.mappers;

import ec.com.bancointernacional.pruebatecnica.dto.CustomerResponse;
import ec.com.bancointernacional.pruebatecnica.entities.Account;
import ec.com.bancointernacional.pruebatecnica.entities.Customer;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CustomerMapper {

    public static CustomerResponse toResponse(Customer customer) {
        if (customer == null) return null;

        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .number(customer.getNumber())
                .identificationNumber(customer.getIdentificationNumber())
                .identificationType(customer.getIdentificationType())
                .account(toAccountResponse(customer.getAccount()))
                .build();
    }

    private static CustomerResponse.AccountResponse toAccountResponse(Account account) {
        if (account == null) return null;

        return CustomerResponse.AccountResponse.builder()
                .number(account.getNumber())
                .type(account.getType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();
    }
}
