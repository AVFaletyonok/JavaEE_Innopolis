--DROP TABLE IF EXISTS accounts, clients, contracts, managers, transactions, flyway_schema_history;

CREATE TABLE IF NOT EXISTS clients(
    "id" bigint primary key not null,
    "first_name" varchar,
    "last_name" varchar,
    "passport_number" varchar,
    "phone_number" varchar
);

COMMENT ON TABLE clients IS 'Its table for clients';
COMMENT ON COLUMN clients.id IS 'The clients ids';
COMMENT ON COLUMN clients.first_name IS 'The clients first names';
COMMENT ON COLUMN clients.last_name IS 'The clients last names';
COMMENT ON COLUMN clients.passport_number IS 'The clients passport numbers';
COMMENT ON COLUMN clients.phone_number IS 'The clients phone numbers';

CREATE TABLE IF NOT EXISTS managers(
    "id" bigint primary key not null,
    "first_name" varchar,
    "last_name" varchar,
    "position" varchar
);

COMMENT ON TABLE managers IS 'Its table for managers';
COMMENT ON COLUMN managers.id IS 'The managers ids';
COMMENT ON COLUMN managers.first_name IS 'The managers first names';
COMMENT ON COLUMN managers.last_name IS 'The managers last names';
COMMENT ON COLUMN managers.position IS 'The managers positions';

CREATE TABLE IF NOT EXISTS contracts(
    "id" bigint primary key not null,
    "id_client" bigint,
    FOREIGN KEY ("id_client") REFERENCES clients(id),
    "id_manager" bigint,
    FOREIGN KEY ("id_manager") REFERENCES managers(id)
);

COMMENT ON TABLE contracts IS 'Its table for contracts';
COMMENT ON COLUMN contracts.id IS 'The contracts ids';
COMMENT ON COLUMN contracts.id_client IS 'The clients ids of the each contract';
COMMENT ON COLUMN contracts.id_manager IS 'The managers ids of the each contract';

CREATE TABLE IF NOT EXISTS accounts(
    "id" bigint primary key not null,
    "id_contract" bigint,
    FOREIGN KEY ("id_contract") REFERENCES contracts(id),
    "amount" decimal
);

COMMENT ON TABLE accounts IS 'Its table for clients accounts';
COMMENT ON COLUMN accounts.id IS 'The accounts ids';
COMMENT ON COLUMN accounts.id_contract IS 'The contracts ids of the each account';
COMMENT ON COLUMN accounts.amount IS 'The amounts on the each account';

CREATE TABLE IF NOT EXISTS transactions(
    "id" bigint primary key not null,
    "id_account" bigint,
    FOREIGN KEY ("id_account") REFERENCES accounts(id),
    "amount" decimal,
    "type" varchar
);

COMMENT ON TABLE transactions IS 'Its table for transactions';
COMMENT ON COLUMN transactions.id IS 'The transactions ids';
COMMENT ON COLUMN transactions.id_account IS 'The accounts ids which make each transaction';
COMMENT ON COLUMN transactions.amount IS 'The amounts of each transaction';
COMMENT ON COLUMN transactions.type IS 'The types of each transaction';