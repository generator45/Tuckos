package tuckos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tuckos.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

}
