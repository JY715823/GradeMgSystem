package cn.edu.ctbu.gmsbackend.repository;

import cn.edu.ctbu.gmsbackend.entity.Enrollment;
import cn.edu.ctbu.gmsbackend.entity.Offering;
import cn.edu.ctbu.gmsbackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);

    List<Enrollment> findByStudentAndOffering_Term(Student student, String term);

    List<Enrollment> findByOffering(Offering offering);

    Optional<Enrollment> findByOfferingAndStudent(Offering offering, Student student);

    int countByOffering(Offering offering);
}
