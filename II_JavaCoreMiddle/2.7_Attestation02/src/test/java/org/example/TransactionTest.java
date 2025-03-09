package org.example;

import org.example.exception.RowWithIdDontExistsException;
import org.example.model.Transaction;
import org.example.repository.Repository;
import org.example.repository.impl.TransactionRepositoryImpl;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionTest {
    private static Repository<Transaction> repo = new TransactionRepositoryImpl();
    private static Transaction transaction =
            new Transaction(7777L, 1L, new BigDecimal(404), "income");
    private static Transaction updatedTransaction =
            new Transaction(7777L, 1L, new BigDecimal(505), "outcome");

    @Test
    public void test1Create() {
        repo.create(transaction);
        assertTrue(repo.checkIsExistById(transaction.getId()));
    }

    @Test(expected = RowWithIdDontExistsException.class)
    public void test2FindByIdException() {
        repo.findById(-1L);
    }

    @Test
    public void test3FindByIdExistRow() {
        try {
            Transaction dbTransaction = repo.findById(transaction.getId());
            assertEquals(transaction.getIdAccount(), dbTransaction.getIdAccount());
            assertEquals(transaction.getAmount(), dbTransaction.getAmount());
            assertEquals(transaction.getType(), dbTransaction.getType());
        } catch (RowWithIdDontExistsException e) {
            fail("Fail by RowWithIdDontExistsException.");
        } catch (DataAccessException e) {
            fail("Fail by DataAccessException.");
        }
    }

    @Test
    public void test4FindAll() {
        Long expectedCount = repo.getCount();
        List<Transaction> transactions = repo.findAll();
        assertEquals(expectedCount.intValue(), transactions.size());
    }

    @Test
    public void test5Update() {
        try {
            repo.update(updatedTransaction);
            Transaction dbUpdatedTransaction = repo.findById(updatedTransaction.getId());
            assertEquals(updatedTransaction.getIdAccount(), dbUpdatedTransaction.getIdAccount());
            assertEquals(updatedTransaction.getAmount(), dbUpdatedTransaction.getAmount());
            assertEquals(updatedTransaction.getType(), dbUpdatedTransaction.getType());
        } catch (RowWithIdDontExistsException e) {
            fail("Fail by RowWithIdDontExistsException.");
        } catch (DataAccessException e) {
            fail("Fail by DataAccessException.");
        }
    }

    @Test
    public void test6DeleteById() {
        repo.deleteById(updatedTransaction.getId());
        assertFalse(repo.checkIsExistById(updatedTransaction.getId()));
    }

    @Ignore // because foreign key
    @Test
    public void test7DeleteAll() {
        repo.deleteAll();
        assertEquals(0L, repo.getCount().longValue());
    }
}
