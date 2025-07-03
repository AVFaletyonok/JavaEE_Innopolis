--DROP TABLE IF EXISTS transactions, accounts, clients, flyway_schema_history;

CREATE TABLE IF NOT EXISTS clients(
    "id" BIGSERIAL primary key not null,
    "first_name" varchar not null,
    "last_name" varchar not null,
    "password" varchar not null,
    "passport_number" varchar not null,
    "phone_number" varchar not null,
    "email" varchar not null,
    "deleted_flag" boolean not null,
    "account_id" bigint
);

COMMENT ON TABLE clients IS 'Its table for clients';
COMMENT ON COLUMN clients.id IS 'The clients ids';
COMMENT ON COLUMN clients.first_name IS 'The clients first names';
COMMENT ON COLUMN clients.last_name IS 'The clients last names';
COMMENT ON COLUMN clients.passport_number IS 'The clients passport numbers';
COMMENT ON COLUMN clients.phone_number IS 'The clients phone numbers';

CREATE TABLE IF NOT EXISTS accounts(
    "id" BIGSERIAL primary key not null,
    "contract_number" varchar not null,
    "amount" decimal
);

COMMENT ON TABLE accounts IS 'Its table for clients accounts';
COMMENT ON COLUMN accounts.id IS 'The accounts ids';
--COMMENT ON COLUMN accounts.id_client IS 'The client id';
COMMENT ON COLUMN accounts.contract_number IS 'The contract number of the each account';
COMMENT ON COLUMN accounts.amount IS 'The amounts on the each account';

ALTER TABLE clients ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(id);

CREATE TABLE IF NOT EXISTS transactions(
    "id" BIGSERIAL primary key not null,
    "account_id" bigint not null,
    FOREIGN KEY ("account_id") REFERENCES accounts(id),
    "receiver_id" bigint not null,
    FOREIGN KEY ("receiver_id") REFERENCES clients(id),
    "amount" decimal not null,
    "type" smallint not null,
    "date_time" timestamp
);

COMMENT ON TABLE transactions IS 'Its table for transactions';
COMMENT ON COLUMN transactions.id IS 'The transactions ids';
COMMENT ON COLUMN transactions.account_id IS 'The accounts ids which make each transaction';
COMMENT ON COLUMN transactions.amount IS 'The amounts of each transaction';
COMMENT ON COLUMN transactions.type IS 'The types of each transaction';