package cn.edu.ctbu.gmsbackend.repository;

import cn.edu.ctbu.gmsbackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentNo(String studentNo);
}
