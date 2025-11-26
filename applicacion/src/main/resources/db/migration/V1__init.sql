CREATE TABLE customers
(
    id                    UUID         NOT NULL,
    name                  VARCHAR(255) NOT NULL,
    number                VARCHAR(255) NOT NULL,
    identification_number VARCHAR(15)  NOT NULL,
    identification_type   SMALLINT     NOT NULL,
    account_number        VARCHAR(20)  NOT NULL,
    account_type          SMALLINT     NOT NULL,
    account_balance       DECIMAL      NOT NULL,
    account_status        SMALLINT     NOT NULL,
    CONSTRAINT pk_customers PRIMARY KEY (id)
);

ALTER TABLE customers
    ADD CONSTRAINT uc_customers_account_number UNIQUE (account_number);

CREATE UNIQUE INDEX idx_account_number ON customers (account_number);

CREATE SEQUENCE customer_number_seq START WITH 1 INCREMENT BY 1;