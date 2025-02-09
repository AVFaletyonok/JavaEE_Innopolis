package org.example.repository;

import org.example.entity.Manager;

import java.util.List;

public interface ManagerRepository {

    List<Manager> findAll();

    void insert(Manager manager);

    void update(Manager manager, String newPosition);

    void delete(Manager manager);
}
