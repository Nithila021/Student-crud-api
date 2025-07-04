package com.example.student_management.service;

import com.example.student_management.dto.StudentDTO;
import com.example.student_management.entity.Student;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.exception.DuplicateResourceException;
import com.example.student_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    // Create Student
    public StudentDTO createStudent(StudentDTO studentDTO) {
        // Check if email already exists
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new DuplicateResourceException("Student with email " + studentDTO.getEmail() + " already exists");
        }
        
        // Check if roll number already exists (if provided)
        if (studentDTO.getRollNumber() != null && studentRepository.existsByRollNumber(studentDTO.getRollNumber())) {
            throw new DuplicateResourceException("Student with roll number " + studentDTO.getRollNumber() + " already exists");
        }
        
        Student student = convertToEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
    }
    
    // Get All Students
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get Student by ID
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return convertToDTO(student);
    }
    
    // Update Student
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        // Check if email is being changed and if new email already exists
        if (!existingStudent.getEmail().equals(studentDTO.getEmail()) && 
            studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new DuplicateResourceException("Student with email " + studentDTO.getEmail() + " already exists");
        }
        
        // Check if roll number is being changed and if new roll number already exists
        if (studentDTO.getRollNumber() != null && 
            !studentDTO.getRollNumber().equals(existingStudent.getRollNumber()) &&
            studentRepository.existsByRollNumber(studentDTO.getRollNumber())) {
            throw new DuplicateResourceException("Student with roll number " + studentDTO.getRollNumber() + " already exists");
        }
        
        // Update fields
        existingStudent.setName(studentDTO.getName());
        existingStudent.setEmail(studentDTO.getEmail());
        existingStudent.setAge(studentDTO.getAge());
        existingStudent.setClassName(studentDTO.getClassName());
        existingStudent.setSection(studentDTO.getSection());
        existingStudent.setRollNumber(studentDTO.getRollNumber());
        existingStudent.setPhoneNumber(studentDTO.getPhoneNumber());
        existingStudent.setDateOfBirth(studentDTO.getDateOfBirth());
        existingStudent.setAddress(studentDTO.getAddress());
        
        Student updatedStudent = studentRepository.save(existingStudent);
        return convertToDTO(updatedStudent);
    }
    
    // Delete Student
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
    }
    
    // Additional methods
    public List<StudentDTO> getStudentsByClass(String className) {
        List<Student> students = studentRepository.findByClassName(className);
        return students.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<StudentDTO> getStudentsByClassAndSection(String className, String section) {
        List<Student> students = studentRepository.findByClassNameAndSection(className, section);
        return students.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<StudentDTO> searchStudentsByName(String name) {
        List<Student> students = studentRepository.findByNameContainingIgnoreCase(name);
        return students.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Helper methods
    private Student convertToEntity(StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setAge(studentDTO.getAge());
        student.setClassName(studentDTO.getClassName());
        student.setSection(studentDTO.getSection());
        student.setRollNumber(studentDTO.getRollNumber());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        student.setDateOfBirth(studentDTO.getDateOfBirth());
        student.setAddress(studentDTO.getAddress());
        return student;
    }
    
    private StudentDTO convertToDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setAge(student.getAge());
        studentDTO.setClassName(student.getClassName());
        studentDTO.setSection(student.getSection());
        studentDTO.setRollNumber(student.getRollNumber());
        studentDTO.setPhoneNumber(student.getPhoneNumber());
        studentDTO.setDateOfBirth(student.getDateOfBirth());
        studentDTO.setAddress(student.getAddress());
        return studentDTO;
    }
}