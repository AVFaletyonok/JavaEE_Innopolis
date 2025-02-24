create table if not exists orders(
    "id" bigint primary key not null,
    "product_number" bigint,
    "count" bigint,
    "amount" decimal,
    "timestamp" timestamp
);