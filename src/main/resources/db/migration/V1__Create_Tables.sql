CREATE TABLE donor (
                       id BIGSERIAL PRIMARY KEY,
                       full_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE beneficiary (
                             id BIGSERIAL PRIMARY KEY,
                             full_name VARCHAR(255) NOT NULL,
                             email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE payment (
                         id VARCHAR(255) PRIMARY KEY,
                         amount NUMERIC(10,2) NOT NULL,
                         status VARCHAR(20) NOT NULL,
                         payer_email VARCHAR(255),
                         psp_type VARCHAR(50)
);

CREATE TABLE donation (
                          id BIGSERIAL PRIMARY KEY,
                          amount NUMERIC(10,2) NOT NULL,
                          date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          donor_id BIGINT NOT NULL REFERENCES donor(id),
                          payment_id VARCHAR(255) REFERENCES payment(id)
);

CREATE TABLE help (
                      id BIGSERIAL PRIMARY KEY,
                      amount NUMERIC(10,2) NOT NULL,
                      description TEXT,
                      date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      beneficiary_id BIGINT NOT NULL REFERENCES beneficiary(id),
                      payment_id VARCHAR(255) REFERENCES payment(id)
);
