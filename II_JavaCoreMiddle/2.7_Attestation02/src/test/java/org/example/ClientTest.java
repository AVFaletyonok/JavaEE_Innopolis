package org.example;

import org.example.exception.RowWithIdDontExistsException;
import org.example.model.Client;
import org.example.repository.Repository;
import org.example.repository.impl.ClientRepositoryImpl;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientTest {
    private static Repository<Client> repo = new ClientRepositoryImpl();
    private static Client client = new Client(7777L, "Anton", "Gorodetskiy",
                                "7777", "+7-777-777-77-77");
    private static Client updatedClient = new Client(7777L, "ANTON", "GORODETSKIY",
                                                     "8888", "+8-888-888-88-88");

    @Test
    public void test1Create() {
        repo.create(client);
        assertTrue(repo.checkIsExistById(client.getId()));
    }

    @Test(expected = RowWithIdDontExistsException.class)
    public void test2FindByIdException() {
        repo.findById(-1L);
    }

    @Test
    public void test3FindByIdExistRow() {
        try {
            Client dbClient = repo.findById(client.getId());
            assertEquals(client.getFirstName(), dbClient.getFirstName());
            assertEquals(client.getLastName(), dbClient.getLastName());
            assertEquals(client.getPassportNumber(), dbClient.getPassportNumber());
            assertEquals(client.getPhoneNumber(), dbClient.getPhoneNumber());
        } catch (RowWithIdDontExistsException e) {
            fail("Fail by RowWithIdDontExistsException.");
        } catch (DataAccessException e) {
            fail("Fail by DataAccessException.");
        }
    }

    @Test
    public void test4FindAll() {
        Long expectedCount = repo.getCount();
        List<Client> clients = repo.findAll();
        assertEquals(expectedCount.intValue(), clients.size());
    }

    @Test
    public void test5Update() {
        try {
            repo.update(updatedClient);
            Client dbUpdatedClient = repo.findById(updatedClient.getId());
            assertEquals(updatedClient.getFirstName(), dbUpdatedClient.getFirstName());
            assertEquals(updatedClient.getLastName(), dbUpdatedClient.getLastName());
            assertEquals(updatedClient.getPassportNumber(), dbUpdatedClient.getPassportNumber());
            assertEquals(updatedClient.getPhoneNumber(), dbUpdatedClient.getPhoneNumber());
        } catch (RowWithIdDontExistsException e) {
            fail("Fail by RowWithIdDontExistsException.");
        } catch (DataAccessException e) {
            fail("Fail by DataAccessException.");
        }
    }

    @Test
    public void test6DeleteById() {
        repo.deleteById(updatedClient.getId());
        assertFalse(repo.checkIsExistById(updatedClient.getId()));
    }

    @Ignore // because foreign key
    @Test
    public void test7DeleteAll() {
        repo.deleteAll();
        assertEquals(0L, repo.getCount().longValue());
    }
}
