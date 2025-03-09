package org.example.repository;

import org.example.model.Transaction;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface Repository<T> {

    Long getCount();

    boolean checkIsExistById(final Long id) throws DataAccessException;

    public boolean checkIsInputRight(T t);

    void create(T t);

    T findById(final Long id) throws DataAccessException;

    List<T> findAll();

    void update(T t);

    void deleteById(final Long id) throws DataAccessException;

    void deleteAll();
}
