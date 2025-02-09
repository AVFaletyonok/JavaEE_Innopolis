CREATE TABLE IF NOT EXISTS clients(
    "id" bigint primary key not null,
    "first_name" varchar,
    "last_name" varchar,
    "passport_number" varchar,
    "phone_number" varchar
);

CREATE TABLE IF NOT EXISTS managers(
    "id" bigint primary key not null,
    "first_name" varchar,
    "last_name" varchar,
    "position" varchar
);

CREATE TABLE IF NOT EXISTS contracts(
    "id" bigint primary key not null,
    "id_client" bigint,
    FOREIGN KEY ("id_client") REFERENCES clients(id),
    "id_manager" bigint,
    FOREIGN KEY ("id_manager") REFERENCES managers(id)
);

CREATE TABLE IF NOT EXISTS accounts(
    "id" bigint primary key not null,
    "id_contract" bigint,
    FOREIGN KEY ("id_contract") REFERENCES contracts(id),
    "amount" decimal
);

CREATE TABLE IF NOT EXISTS transactions(
    "id" bigint primary key not null,
    "id_account" bigint,
    FOREIGN KEY ("id_account") REFERENCES accounts(id),
    "amount" decimal,
    "type" varchar
);