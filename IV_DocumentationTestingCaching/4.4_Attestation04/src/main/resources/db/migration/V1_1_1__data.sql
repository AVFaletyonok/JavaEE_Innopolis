INSERT INTO accounts (contract_number, amount) VALUES ('2025-06-29-1', 2000);
INSERT INTO accounts (contract_number, amount) VALUES ('2025-06-29-2', 1000);
INSERT INTO accounts (contract_number, amount) VALUES ('2025-06-29-3', 1000);
INSERT INTO clients (first_name, last_name, password, passport_number, phone_number, deleted_flag, account_id)
             VALUES ('John', 'Lennon', '123', '1234:000001', '+1-999-999-99-01', false, 1);
INSERT INTO clients (first_name, last_name, password, passport_number, phone_number, deleted_flag, account_id)
             VALUES ('John', 'Travolta', '123', '1234:000002', '+1-999-999-99-02', false, 2);
INSERT INTO clients (first_name, last_name, password, passport_number, phone_number, deleted_flag, account_id)
             VALUES ('Dmitriy', 'Mendeleev', '123', '1234:000003', '+1-999-999-99-03', false, 3);