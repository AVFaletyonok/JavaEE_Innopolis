package org.example.repository;

import org.example.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long>,
        PagingAndSortingRepository<StudentEntity, Long>,
        JpaSpecificationExecutor<StudentEntity> {

//    @Query("""
//            SELECT s FROM StudentEntity s \
//            LEFT JOIN FETCH s.reviews \
//            LEFT JOIN FETCH s.courses WHERE s.id = :id""")
//    Optional<StudentEntity> findByIdWithReviewsAndCourses(@Param("id") Long id);

    @Query("SELECT s FROM StudentEntity s WHERE s.birthDate < :date")
    List<StudentEntity> findStudentsOlderThan(@Param("date") LocalDate date);

//    @Query("SELECT s FROM StudentEntity s, StudentGroupEntity sqe " +
//            "WHERE sge.student.id = s.id and count(sge) > :groupCount")
    @Query("""
            SELECT s FROM StudentEntity s \
            WHERE SIZE(s.courses) >= :courseCount""")
    List<StudentEntity> findStudentsByCourseCount(@Param("courseCount") Long courseCount);

    @Query(value = """
            SELECT s FROM StudentEntity s WHERE s.firstName = :studentName \
            AND EXISTS (SELECT c FROM s.courses c WHERE c.courseId = :courseId)""")
    List<StudentEntity> findAllByNameAndCourseId(@Param("studentName") String studentName,
                                                 @Param("courseId") Long courseId);
}
