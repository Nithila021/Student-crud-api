package com.example.student_management.repository;


import com.example.student_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Find by email
    Optional<Student> findByEmail(String email);
    
    // Find by class and section
    List<Student> findByClassNameAndSection(String className, String section);
    
    // Find by class
    List<Student> findByClassName(String className);
    
    // Find by roll number
    Optional<Student> findByRollNumber(String rollNumber);
    
    // Custom query to find students by name containing
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> findByNameContainingIgnoreCase(@Param("name") String name);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if roll number exists
    boolean existsByRollNumber(String rollNumber);
}
