package org.example;

import org.example.exception.RowWithIdDontExistsException;
import org.example.model.Manager;
import org.example.repository.Repository;
import org.example.repository.impl.ManagerRepositoryImpl;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManagerTest {
    private static Repository<Manager> repo = new ManagerRepositoryImpl();
    private static Manager manager =
            new Manager(7777L, "Anton", "Gorodetskiy", "main manager");
    private static Manager updatedManager =
            new Manager(7777L, "ANTON", "GORODETSKIY", "great main manager");

    @Test
    public void test1Create() {
        repo.create(manager);
        assertTrue(repo.checkIsExistById(manager.getId()));
    }

    @Test(expected = RowWithIdDontExistsException.class)
    public void test2FindByIdException() {
        repo.findById(-1L);
    }

    @Test
    public void test3FindByIdExistRow() {
        try {
            Manager dbManager = repo.findById(manager.getId());
            assertEquals(manager.getFirstName(), dbManager.getFirstName());
            assertEquals(manager.getLastName(), dbManager.getLastName());
            assertEquals(manager.getPosition(), dbManager.getPosition());
        } catch (RowWithIdDontExistsException e) {
            fail("Fail by RowWithIdDontExistsException.");
        } catch (DataAccessException e) {
            fail("Fail by DataAccessException.");
        }
    }

    @Test
    public void test4FindAll() {
        Long expectedCount = repo.getCount();
        List<Manager> managers = repo.findAll();
        assertEquals(expectedCount.intValue(), managers.size());
    }

    @Test
    public void test5Update() {
        try {
            repo.update(updatedManager);
            Manager dbUpdatedManager = repo.findById(updatedManager.getId());
            assertEquals(updatedManager.getFirstName(), dbUpdatedManager.getFirstName());
            assertEquals(updatedManager.getLastName(), dbUpdatedManager.getLastName());
            assertEquals(updatedManager.getPosition(), dbUpdatedManager.getPosition());
        } catch (RowWithIdDontExistsException e) {
            fail("Fail by RowWithIdDontExistsException.");
        } catch (DataAccessException e) {
            fail("Fail by DataAccessException.");
        }
    }

    @Test
    public void test6DeleteById() {
        repo.deleteById(updatedManager.getId());
        assertFalse(repo.checkIsExistById(updatedManager.getId()));
    }

    @Ignore // because foreign key
    @Test
    public void test7DeleteAll() {
        repo.deleteAll();
        assertEquals(0L, repo.getCount().longValue());
    }
}
