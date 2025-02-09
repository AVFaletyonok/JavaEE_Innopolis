INSERT INTO clients (id, first_name, last_name, passport_number, phone_number)
             VALUES (1, 'John', 'Lennon', '1234', '+1-999-999-99-99');
INSERT INTO clients (id, first_name, last_name, passport_number, phone_number)
             VALUES (2, 'John', 'Travolta', '1234', '+1-999-999-99-99');
INSERT INTO clients (id, first_name, last_name, passport_number, phone_number)
             VALUES (3, 'Dmitriy', 'Mendeleev', '1234', '+7-999-999-99-99');
INSERT INTO clients (id, first_name, last_name, passport_number, phone_number)
             VALUES (4, 'Charles', 'Darwin', '1234', '+44-999-999-99-99');
INSERT INTO clients (id, first_name, last_name, passport_number, phone_number)
             VALUES (5, 'Leonardo', 'DaVinci', '1234', '+39-999-999-99-99');
INSERT INTO clients (id, first_name, last_name, passport_number, phone_number)
             VALUES (6, 'Andrey', 'Mironov', '2345', '+7-999-999-99-99');
INSERT INTO clients (id, first_name, last_name, passport_number, phone_number)
             VALUES (7, 'Vasiliy', 'Dontsov', '5123', '+7-999-999-99-99');
INSERT INTO clients (id, first_name, last_name, passport_number, phone_number)
             VALUES (8, 'Igor', 'Vorobiev', '1234', '+7-999-999-99-99');
INSERT INTO clients (id, first_name, last_name, passport_number, phone_number)
             VALUES (9, 'Anton', 'Ivanov', '5555', '+7-999-999-99-99');
INSERT INTO clients (id, first_name, last_name, passport_number, phone_number)
             VALUES (10, 'Peter', 'Petrov', '2233', '+7-999-999-99-99');
INSERT INTO managers (id, first_name, last_name, position)
              VALUES (1, 'Anton', 'Gorodetsky', 'personal manager');
INSERT INTO managers (id, first_name, last_name, position)
              VALUES (2, 'Michael', 'Krug', 'main manager');
INSERT INTO managers (id, first_name, last_name, position)
              VALUES (3, 'Ivan', 'Borisov', 'main manager');
INSERT INTO managers (id, first_name, last_name, position)
              VALUES (4, 'Sergey', 'Vasin', 'personal manager');
INSERT INTO managers (id, first_name, last_name, position)
              VALUES (5, 'Ilya', 'Sidorov', 'main manager');
INSERT INTO managers (id, first_name, last_name, position)
              VALUES (6, 'Ibragim', 'Pechkin', 'main manager');
INSERT INTO managers (id, first_name, last_name, position)
              VALUES (7, 'Genadiy', 'Vazhniy', 'personal manager');
INSERT INTO managers (id, first_name, last_name, position)
              VALUES (8, 'Igor', 'Petrov', 'main manager');
INSERT INTO managers (id, first_name, last_name, position)
              VALUES (9, 'Maksim', 'Porechniy', 'personal manager');
INSERT INTO managers (id, first_name, last_name, position)
              VALUES (10, 'Vasiliy', 'Petrov', 'main manager');
INSERT INTO contracts (id, id_client, id_manager) VALUES (1, 1, 3);
INSERT INTO contracts (id, id_client, id_manager) VALUES (2, 2, 1);
INSERT INTO contracts (id, id_client, id_manager) VALUES (3, 3, 2);
INSERT INTO contracts (id, id_client, id_manager) VALUES (4, 4, 5);
INSERT INTO contracts (id, id_client, id_manager) VALUES (5, 5, 4);
INSERT INTO contracts (id, id_client, id_manager) VALUES (6, 6, 6);
INSERT INTO contracts (id, id_client, id_manager) VALUES (7, 7, 8);
INSERT INTO contracts (id, id_client, id_manager) VALUES (8, 8, 9);
INSERT INTO contracts (id, id_client, id_manager) VALUES (9, 9, 10);
INSERT INTO contracts (id, id_client, id_manager) VALUES (10, 10, 7);
INSERT INTO accounts (id, id_contract, amount) VALUES (1, 1, 2000);
INSERT INTO accounts (id, id_contract, amount) VALUES (2, 2, 1000);
INSERT INTO accounts (id, id_contract, amount) VALUES (3, 3, 5000);
INSERT INTO accounts (id, id_contract, amount) VALUES (4, 4, 1000);
INSERT INTO accounts (id, id_contract, amount) VALUES (5, 5, 8000);
INSERT INTO accounts (id, id_contract, amount) VALUES (6, 6, 3000);
INSERT INTO accounts (id, id_contract, amount) VALUES (7, 7, 4500);
INSERT INTO accounts (id, id_contract, amount) VALUES (8, 8, 9000);
INSERT INTO accounts (id, id_contract, amount) VALUES (9, 9, 4700);
INSERT INTO accounts (id, id_contract, amount) VALUES (10, 10, 1000);
INSERT INTO transactions (id, id_account, amount, type) VALUES (1, 1, 400, 'income');
INSERT INTO transactions (id, id_account, amount, type) VALUES (2, 2, 250, 'outcome');
INSERT INTO transactions (id, id_account, amount, type) VALUES (3, 3, 600, 'income');
INSERT INTO transactions (id, id_account, amount, type) VALUES (4, 4, 500, 'income');
INSERT INTO transactions (id, id_account, amount, type) VALUES (5, 5, 100, 'outcome');
INSERT INTO transactions (id, id_account, amount, type) VALUES (6, 6, 350, 'outcome');
INSERT INTO transactions (id, id_account, amount, type) VALUES (7, 7, 400, 'income');
INSERT INTO transactions (id, id_account, amount, type) VALUES (8, 8, 700, 'outcome');
INSERT INTO transactions (id, id_account, amount, type) VALUES (9, 9, 500, 'income');
INSERT INTO transactions (id, id_account, amount, type) VALUES (10, 10, 300, 'income');