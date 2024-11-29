package tuckos.service;

import org.springframework.stereotype.Service;

import tuckos.entity.Student;
import tuckos.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    public Student getStudent(String rollNo) {
        return studentRepository.findById(rollNo).orElse(null);
    }

    public void deleteStudent(String rollNo) {
        studentRepository.deleteById(rollNo);
    }
}
