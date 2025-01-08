select * from accounts;
insert into accounts (id, id_contract, amount) values (1, 1, 2000);
update accounts set amount = 5000 where id = 4;
delete from accounts where id = 5;

select * from clients;
insert into clients (id, first_name, last_name, passport_number, phone_number) values (1, 'John', 'Lennon', '1234', '+1-999-999-99-99');
update clients set phone_number = '+7-999-999-99-99' where id = 2;
delete from clients where id = 7;

select * from contracts;
insert into contracts (id, id_client, id_manager) values (1, 1, 3);
update contracts set id_manager = 5 where id = 2;
delete from contracts where id = 4;

select * from managers;
insert into managers (id, first_name, last_name, position) values (1, 'Anton', 'Gorodetsky', 'personal manager');
update managers set position = 'personal manager' where id = 3;
delete from managers where id = 3;

select * from transactions;
insert into transactions (id, id_account, amount, type) values (1, 1, 400, 'income');
update transactions set amount = 300 where id = 5;
delete from transactions where id = 10;
