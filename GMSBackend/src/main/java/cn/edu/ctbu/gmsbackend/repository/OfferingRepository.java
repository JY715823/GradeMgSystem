package cn.edu.ctbu.gmsbackend.repository;

import cn.edu.ctbu.gmsbackend.entity.Offering;
import cn.edu.ctbu.gmsbackend.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferingRepository extends JpaRepository<Offering, Long> {
    List<Offering> findByTeacherAndTerm(Teacher teacher, String term);
    List<Offering> findByTeacher(Teacher teacher);

    @Query("select o from Offering o where (:term is null or o.term = :term) and " +
            "(:keyword is null or lower(o.course.name) like lower(concat('%', :keyword, '%')) " +
            "or lower(o.teacher.name) like lower(concat('%', :keyword, '%')))")
    List<Offering> searchByTermAndKeyword(String term, String keyword);

    List<Offering> findByTerm(String term);

    @Query("select distinct o.term from Offering o order by o.term desc")
    List<String> findDistinctTerms();
}
