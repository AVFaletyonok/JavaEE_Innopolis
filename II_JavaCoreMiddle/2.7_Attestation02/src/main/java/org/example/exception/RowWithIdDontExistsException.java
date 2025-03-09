package org.example.exception;

import org.springframework.dao.DataAccessException;

public class RowWithIdDontExistsException extends DataAccessException {

    public RowWithIdDontExistsException(String message, final Long id) {
        super(message + " with requested id = " + id + " doesn't exist.");
    }

    public RowWithIdDontExistsException(String message, final Long id, Throwable cause) {
        super(message + " with requested id = " + id + " doesn't exist.", cause);
    }
}
