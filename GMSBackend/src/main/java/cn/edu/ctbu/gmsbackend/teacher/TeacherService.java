package cn.edu.ctbu.gmsbackend.teacher;

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
public class TeacherService {

    private final OfferingRepository offeringRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    private Teacher currentTeacher() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new BusinessException(4010, "未登录");
        }
        UserAccount user = userDetails.getUser();
        if (user.getRole() != UserRole.TEACHER) {
            throw new BusinessException(4030, "仅教师可访问");
        }
        return teacherRepository.findById(user.getProfileId())
                .orElseThrow(() -> new BusinessException(4040, "教师不存在"));
    }

    public List<TeacherOfferingDto> myOfferings(String term) {
        Teacher teacher = currentTeacher();
        List<Offering> list = term == null ? offeringRepository.findByTeacher(teacher) :
                offeringRepository.findByTeacherAndTerm(teacher, term);
        return list.stream().map(o -> new TeacherOfferingDto(
                o.getId(), o.getTerm(), o.getCourse().getName(), o.getClassLabel(),
                o.getWeekday() + " " + o.getStartPeriod() + "-" + o.getEndPeriod() + " " + o.getLocation(),
                enrollmentRepository.countByOffering(o)
        )).collect(Collectors.toList());
    }

    public List<OfferingStudentDto> offeringStudents(Long offeringId) {
        Offering offering = validateOwnership(offeringId);
        List<Enrollment> enrollments = enrollmentRepository.findByOffering(offering);
        return enrollments.stream().map(e -> new OfferingStudentDto(
                e.getStudent().getId(),
                e.getStudent().getStudentNo(),
                e.getStudent().getName(),
                e.getStudent().getClassName(),
                gradeRepository.findByOfferingAndStudent(offering, e.getStudent())
                        .map(g -> g.getScore() != null ? g.getScore().toPlainString() : null)
                        .orElse(null)
        )).collect(Collectors.toList());
    }

    @Transactional
    public void saveDraft(Long offeringId, List<GradeInput> inputs) {
        Offering offering = validateOwnership(offeringId);
        for (GradeInput input : inputs) {
            Student student = studentRepository.findById(input.getStudentId())
                    .orElseThrow(() -> new BusinessException(4040, "学生不存在"));
            if (enrollmentRepository.findByOfferingAndStudent(offering, student).isEmpty()) {
                continue;
            }
            Grade grade = gradeRepository.findByOfferingAndStudent(offering, student)
                    .orElseGet(() -> {
                        Grade g = new Grade();
                        g.setOffering(offering);
                        g.setStudent(student);
                        return g;
                    });
            grade.setScore(input.getScore());
            grade.setStatus(GradeStatus.DRAFT);
            gradeRepository.save(grade);
        }
    }

    @Transactional
    public void publish(Long offeringId) {
        Offering offering = validateOwnership(offeringId);
        List<Grade> grades = gradeRepository.findByOffering(offering);
        for (Grade grade : grades) {
            grade.setStatus(GradeStatus.PUBLISHED);
        }
        gradeRepository.saveAll(grades);
    }

    private Offering validateOwnership(Long offeringId) {
        Teacher teacher = currentTeacher();
        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new BusinessException(4040, "开课不存在"));
        if (!offering.getTeacher().getId().equals(teacher.getId())) {
            throw new BusinessException(4030, "无权访问");
        }
        return offering;
    }
}
