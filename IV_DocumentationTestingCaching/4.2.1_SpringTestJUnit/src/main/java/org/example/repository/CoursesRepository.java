package org.example.repository;

import org.example.entity.CoursesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursesRepository extends JpaRepository<CoursesEntity, Long> {

//    @Query("SELECT COUNT(c) > 0 FROM CoursesEntity c WHERE c.name = :name")
//    Boolean isCourseExists(@Param("name") String name);

    @Query("SELECT c FROM CoursesEntity c WHERE c.courseId = :courseId")
    CoursesEntity findByCourseId(@Param("courseId") Long courseId);
}
