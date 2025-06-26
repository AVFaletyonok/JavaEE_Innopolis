package org.example.repository;

import org.example.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для хранения задач.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
