package cn.edu.ctbu.gmsbackend.repository;

import cn.edu.ctbu.gmsbackend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByOfferingAndStudent(Offering offering, Student student);

    List<Grade> findByStudentAndOffering_Term(Student student, String term);

    List<Grade> findByOffering(Offering offering);
}
