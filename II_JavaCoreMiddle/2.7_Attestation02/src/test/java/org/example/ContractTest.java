package org.example;

import org.example.exception.RowWithIdDontExistsException;
import org.example.model.Contract;
import org.example.repository.Repository;
import org.example.repository.impl.ContractRepositoryImpl;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContractTest {
    private static Repository<Contract> repo = new ContractRepositoryImpl();
    private static Contract contract = new Contract(77L, 1L, 1L);
    private static Contract updatedContract = new Contract(77L, 2L, 2L);

    @Test
    public void test1Create() {
        repo.create(contract);
        assertTrue(repo.checkIsExistById(contract.getId()));
    }

    @Test(expected = RowWithIdDontExistsException.class)
    public void test2FindByIdException() {
        repo.findById(-1L);
    }

    @Test
    public void test3FindByIdExistRow() {
        try {
            Contract dbContract = repo.findById(contract.getId());
            assertEquals(contract.getIdClient(), dbContract.getIdClient());
            assertEquals(contract.getIdManager(), dbContract.getIdManager());
        } catch (RowWithIdDontExistsException e) {
            fail("Fail by RowWithIdDontExistsException.");
        } catch (DataAccessException e) {
            fail("Fail by DataAccessException.");
        }
    }

    @Test
    public void test4FindAll() {
        Long expectedCount = repo.getCount();
        List<Contract> contracts = repo.findAll();
        assertEquals(expectedCount.intValue(), contracts.size());
    }

    @Test
    public void test5Update() {
        try {
            repo.update(updatedContract);
            Contract dbUpdatedContract = repo.findById(updatedContract.getId());
            assertEquals(updatedContract.getIdClient(), dbUpdatedContract.getIdClient());
            assertEquals(updatedContract.getIdManager(), dbUpdatedContract.getIdManager());
        } catch (RowWithIdDontExistsException e) {
            fail("Fail by RowWithIdDontExistsException.");
        } catch (DataAccessException e) {
            fail("Fail by DataAccessException.");
        }
    }

    @Test
    public void test6DeleteById() {
        repo.deleteById(updatedContract.getId());
        assertFalse(repo.checkIsExistById(updatedContract.getId()));
    }

    @Ignore // because foreign key
    @Test
    public void test7DeleteAll() {
        repo.deleteAll();
        assertEquals(0L, repo.getCount().longValue());
    }
}
