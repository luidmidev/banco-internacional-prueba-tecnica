package ec.com.bancointernacional.pruebatecnica.dto;

import ec.com.bancointernacional.pruebatecnica.schemas.AccountStatus;
import ec.com.bancointernacional.pruebatecnica.schemas.AccountType;
import ec.com.bancointernacional.pruebatecnica.schemas.IdentificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private UUID id;
    private String name;
    private String number;
    private String identificationNumber;
    private IdentificationType identificationType;
    private AccountResponse account;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AccountResponse {
        private String number;
        private AccountType type;
        private BigDecimal balance;
        private AccountStatus status;
    }
}
