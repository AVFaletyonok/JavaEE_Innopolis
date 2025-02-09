package org.example;

import org.example.entity.*;
import org.example.repository.*;
import org.example.repository.impl.*;

import java.math.BigDecimal;
import java.util.List;

public class Main
{
    private static final AccountRepository accountRepository = new AccountRepositoryImpl();
    private static final ClientRepository clientRepository = new ClientRepositoryImpl();
    private static final ContractRepository contractRepository = new ContractRepositoryImpl();
    private static final ManagerRepository managerRepository = new ManagerRepositoryImpl();
    private static final TransactionRepository transactionRepository = new TransactionRepositoryImpl();

    public static void main( String[] args )
    {
        List<Client> clients = clientRepository.findAll();
        System.out.println("Clients:\n" + clients);
        Client newClient = new Client(Long.valueOf(11), "Dmitry", "Andreev",
                "1234", "+7-999-999-99-99");
        clientRepository.insert(newClient);
        clients = clientRepository.findAll();
        System.out.println("Clients after inserting 11th client:\n" + clients);
        clientRepository.updatePhoneNumber(newClient, "+1-111-111-11-11");
        clients = clientRepository.findAll();
        System.out.println("Clients after updating 11th client's phone number:\n" + clients);
        clientRepository.delete(newClient);
        clients = clientRepository.findAll();
        System.out.println("Clients after deleting 11th client:\n" + clients);

        List<Manager> managers = managerRepository.findAll();
        System.out.println("\nManagers:\n" + managers);
        Manager newManager = new Manager(
                Long.valueOf(11), "Ivan", "Petrov", "person manager");
        managerRepository.insert(newManager);
        managers = managerRepository.findAll();
        System.out.println("Managers after inserting 11th manager:\n" + managers);
        managerRepository.update(newManager, "main manager");
        managers = managerRepository.findAll();
        System.out.println("Managers after updating 11th manager's position:\n" + managers);
        managerRepository.delete(newManager);
        managers = managerRepository.findAll();
        System.out.println("Managers after deleting 11th manager:\n" + managers);

        List<Contract> contracts = contractRepository.findAll();
        System.out.println("\nContracts:\n" + contracts);
        Contract newContract = new Contract(Long.valueOf(11), Long.valueOf(2), Long.valueOf(1));
        contractRepository.insert(newContract);
        contracts = contractRepository.findAll();
        System.out.println("Contracts after inserting new 11th contract:\n" + contracts);
        contractRepository.update(newContract, Long.valueOf(2));
        contracts = contractRepository.findAll();
        System.out.println("Contracts after updating manager for the 11th contract:\n" + contracts);
        contractRepository.delete(newContract);
        contracts = contractRepository.findAll();
        System.out.println("Contracts after deleting 11th contract:\n" + contracts);

        List<Account> accounts = accountRepository.findAll();
        System.out.println("\nAccounts:\n" + accounts);
        Account newAccount = new Account(Long.valueOf(11), Long.valueOf(5), new BigDecimal("600"));
        accountRepository.insert(newAccount);
        accounts = accountRepository.findAll();
        System.out.println("Accounts after inserting new 11th account:\n" + accounts);
        accountRepository.update(newAccount, new BigDecimal("10000"));
        accounts = accountRepository.findAll();
        System.out.println("Accounts after updating amount of the 11th account:\n" + accounts);
        accountRepository.delete(newAccount);
        accounts = accountRepository.findAll();
        System.out.println("Accounts after deleting 11th account:\n" + accounts);

        List<Transaction> transactions = transactionRepository.findAll();
        System.out.println("\nTransactions:\n" + transactions);
        Transaction newTransaction = new Transaction(Long.valueOf(11), Long.valueOf(4),
                                                    new BigDecimal("300"), "income");
        transactionRepository.insert(newTransaction);
        transactions = transactionRepository.findAll();
        System.out.println("Transactions after inserting new 11th transaction:\n" + transactions);
        transactionRepository.update(newTransaction, new BigDecimal("5000"));
        transactions = transactionRepository.findAll();
        System.out.println("Transactions after updating amount of the 11th transaction:\n" + transactions);
        transactionRepository.delete(newTransaction);
        transactions = transactionRepository.findAll();
        System.out.println("Transactions after deleting 11th transaction:\n" + transactions);
    }
}
