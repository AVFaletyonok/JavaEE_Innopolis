package org.example;

import org.example.exception.RowWithIdDontExistsException;
import org.example.model.Account;
import org.example.repository.Repository;
import org.example.repository.impl.AccountRepositoryImpl;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountTest {
    private static Repository<Account> repo = new AccountRepositoryImpl();
    private static Account account = new Account(777L, 1L,  new BigDecimal(101));
    private static Account updatedAccount = new Account(777L, 2L,  new BigDecimal(212));

    @Test
    public void test1Create() {
        repo.create(account);
        assertTrue(repo.checkIsExistById(account.getId()));
    }

    @Test(expected = RowWithIdDontExistsException.class)
    public void test2FindByIdException() {
        repo.findById(-1L);
    }

    @Test
    public void test3FindByIdExistRow() {
        try {
            Account dbAccount = repo.findById(account.getId());
            assertEquals(account.getIdContract(), dbAccount.getIdContract());
            assertEquals(account.getAmount(), dbAccount.getAmount());
        } catch (RowWithIdDontExistsException e) {
            fail("Fail by RowWithIdDontExistsException.");
        } catch (DataAccessException e) {
            fail("Fail by DataAccessException.");
        }
    }

    @Test
    public void test4FindAll() {
        Long expectedCount = repo.getCount();
        List<Account> accounts = repo.findAll();
        assertEquals(expectedCount.intValue(), accounts.size());
    }

    @Test
    public void test5Update() {
        try {
            repo.update(updatedAccount);
            Account dbUpdatedAccount = repo.findById(updatedAccount.getId());
            assertEquals(updatedAccount.getIdContract(), dbUpdatedAccount.getIdContract());
            assertEquals(updatedAccount.getAmount(), dbUpdatedAccount.getAmount());
        } catch (RowWithIdDontExistsException e) {
            fail("Fail by RowWithIdDontExistsException.");
        } catch (DataAccessException e) {
            fail("Fail by DataAccessException.");
        }
    }

    @Test
    public void test6DeleteById() {
        repo.deleteById(updatedAccount.getId());
        assertFalse(repo.checkIsExistById(updatedAccount.getId()));
    }

    @Ignore // because foreign key
    @Test
    public void test7DeleteAll() {
        repo.deleteAll();
        assertEquals(0L, repo.getCount().longValue());
    }
}
