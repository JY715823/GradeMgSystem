package cn.edu.ctbu.gmsbackend.student;

import cn.edu.ctbu.gmsbackend.common.BusinessException;
import cn.edu.ctbu.gmsbackend.entity.*;
import cn.edu.ctbu.gmsbackend.repository.*;
import cn.edu.ctbu.gmsbackend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final OfferingRepository offeringRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;

    private Student currentStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new BusinessException(4010, "未登录");
        }
        UserAccount user = userDetails.getUser();
        if (user.getRole() != UserRole.STUDENT) {
            throw new BusinessException(4030, "仅学生可访问");
        }
        return studentRepository.findById(user.getProfileId())
                .orElseThrow(() -> new BusinessException(4040, "学生不存在"));
    }

    public List<OfferingDto> listOfferings(String term, String keyword) {
        List<Offering> offerings = offeringRepository.searchByTermAndKeyword(term, keyword);
        return offerings.stream().map(o -> new OfferingDto(
                o.getId(), o.getTerm(), o.getCourse().getName(), o.getTeacher().getName(),
                o.getClassLabel(), o.getWeekday(), o.getStartPeriod(), o.getEndPeriod(),
                o.getStartWeek(), o.getEndWeek(), o.getLocation(), o.getCapacity(),
                enrollmentRepository.countByOffering(o)
        )).collect(Collectors.toList());
    }

    @Transactional
    public void enroll(Long offeringId) {
        Student student = currentStudent();
        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new BusinessException(4040, "开课不存在"));
        if (enrollmentRepository.findByOfferingAndStudent(offering, student).isPresent()) {
            throw new BusinessException(4092, "已选该课程");
        }
        int count = enrollmentRepository.countByOffering(offering);
        if (count >= offering.getCapacity()) {
            throw new BusinessException(4093, "容量已满");
        }
        List<Enrollment> existing = enrollmentRepository.findByStudentAndOffering_Term(student, offering.getTerm());
        boolean conflict = existing.stream().anyMatch(e -> {
            Offering o = e.getOffering();
            boolean weekOverlap = Math.max(o.getStartWeek(), offering.getStartWeek()) <= Math.min(o.getEndWeek(), offering.getEndWeek());
            boolean periodOverlap = Math.max(o.getStartPeriod(), offering.getStartPeriod()) <= Math.min(o.getEndPeriod(), offering.getEndPeriod());
            return o.getWeekday().equals(offering.getWeekday()) && weekOverlap && periodOverlap;
        });
        if (conflict) {
            throw new BusinessException(4094, "时间冲突");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setOffering(offering);
        enrollment.setStudent(student);
        enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void drop(Long offeringId) {
        Student student = currentStudent();
        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new BusinessException(4040, "开课不存在"));
        Enrollment enrollment = enrollmentRepository.findByOfferingAndStudent(offering, student)
                .orElseThrow(() -> new BusinessException(4041, "未选该课程"));
        enrollmentRepository.delete(enrollment);
    }

    public List<TimetableItem> timetable(String term) {
        Student student = currentStudent();
        List<Enrollment> enrollments = enrollmentRepository.findByStudentAndOffering_Term(student, term);
        return enrollments.stream().map(e -> {
            Offering o = e.getOffering();
            return new TimetableItem(o.getId(), o.getCourse().getName(), o.getTeacher().getName(),
                    o.getWeekday(), o.getStartPeriod(), o.getEndPeriod(), o.getStartWeek(),
                    o.getEndWeek(), o.getLocation());
        }).collect(Collectors.toList());
    }

    public List<StudentGradeResponse> grades(String term) {
        Student student = currentStudent();
        List<Enrollment> enrollments = enrollmentRepository.findByStudentAndOffering_Term(student, term);
        return enrollments.stream().map(e -> {
            Grade grade = gradeRepository.findByOfferingAndStudent(e.getOffering(), student).orElse(null);
            GradeStatus status = grade != null ? grade.getStatus() : GradeStatus.DRAFT;
            return new StudentGradeResponse(
                    e.getOffering().getId(),
                    e.getOffering().getCourse().getName(),
                    e.getOffering().getTeacher().getName(),
                    status == GradeStatus.PUBLISHED && grade != null ? grade.getScore() : null,
                    status.name()
            );
        }).collect(Collectors.toList());
    }
}
