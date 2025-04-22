package org.example.repository;

import org.example.entity.StudentGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroupEntity, Long> {

    @Query("SELECT COUNT(g) > 0 FROM StudentGroupEntity g WHERE g.name = :name")
    Boolean isGroupExists(@Param("name") String name);

    @Query("SELECT g FROM StudentGroupEntity g WHERE g.name = :name")
    StudentGroupEntity findByName(@Param("name") String name);
}
