package cn.edu.ctbu.gmsbackend.admin;

import cn.edu.ctbu.gmsbackend.auth.AuthService;
import cn.edu.ctbu.gmsbackend.common.BusinessException;
import cn.edu.ctbu.gmsbackend.entity.*;
import cn.edu.ctbu.gmsbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final OfferingRepository offeringRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordResetRequestRepository passwordResetRequestRepository;
    private final AuthService authService;

    private static final String DEFAULT_PASSWORD = "123456";

    // Student CRUD
    public List<Student> listStudents() {
        return studentRepository.findAll();
    }

    @Transactional
    public Student createStudent(StudentRequest request) {
        studentRepository.findByStudentNo(request.getStudentNo())
                .ifPresent(s -> { throw new BusinessException(4090, "学号已存在"); });
        Student student = new Student();
        student.setStudentNo(request.getStudentNo());
        student.setName(request.getName());
        student.setClassName(request.getClassName());
        student.setPhone(request.getPhone());
        student.setEmail(request.getEmail());
        Student saved = studentRepository.save(student);
        authService.createUser(saved.getStudentNo(), UserRole.STUDENT, "STUDENT", saved.getId(), DEFAULT_PASSWORD);
        return saved;
    }

    @Transactional
    public Student updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(4040, "学生不存在"));
        student.setStudentNo(request.getStudentNo());
        student.setName(request.getName());
        student.setClassName(request.getClassName());
        student.setPhone(request.getPhone());
        student.setEmail(request.getEmail());
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public void resetStudentPassword(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(4040, "学生不存在"));
        UserAccount account = userAccountRepository.findByUsernameAndRole(student.getStudentNo(), UserRole.STUDENT)
                .orElseGet(() -> authService.createUser(student.getStudentNo(), UserRole.STUDENT, "STUDENT", student.getId(), DEFAULT_PASSWORD));
        authService.resetPassword(account, DEFAULT_PASSWORD);
    }

    // Teacher CRUD
    public List<Teacher> listTeachers() {
        return teacherRepository.findAll();
    }

    @Transactional
    public Teacher createTeacher(TeacherRequest request) {
        teacherRepository.findByTeacherNo(request.getTeacherNo())
                .ifPresent(t -> { throw new BusinessException(4090, "工号已存在"); });
        Teacher teacher = new Teacher();
        teacher.setTeacherNo(request.getTeacherNo());
        teacher.setName(request.getName());
        teacher.setDept(request.getDept());
        teacher.setPhone(request.getPhone());
        teacher.setEmail(request.getEmail());
        Teacher saved = teacherRepository.save(teacher);
        authService.createUser(saved.getTeacherNo(), UserRole.TEACHER, "TEACHER", saved.getId(), DEFAULT_PASSWORD);
        return saved;
    }

    @Transactional
    public Teacher updateTeacher(Long id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(4040, "教师不存在"));
        teacher.setTeacherNo(request.getTeacherNo());
        teacher.setName(request.getName());
        teacher.setDept(request.getDept());
        teacher.setPhone(request.getPhone());
        teacher.setEmail(request.getEmail());
        return teacherRepository.save(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    @Transactional
    public void resetTeacherPassword(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(4040, "教师不存在"));
        UserAccount account = userAccountRepository.findByUsernameAndRole(teacher.getTeacherNo(), UserRole.TEACHER)
                .orElseGet(() -> authService.createUser(teacher.getTeacherNo(), UserRole.TEACHER, "TEACHER", teacher.getId(), DEFAULT_PASSWORD));
        authService.resetPassword(account, DEFAULT_PASSWORD);
    }

    // Course CRUD
    public List<Course> listCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public Course createCourse(CourseRequest request) {
        courseRepository.findByCourseCode(request.getCourseCode())
                .ifPresent(c -> { throw new BusinessException(4090, "课程号已存在"); });
        Course course = new Course();
        course.setCourseCode(request.getCourseCode());
        course.setName(request.getName());
        course.setCredit(request.getCredit());
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessException(4040, "课程不存在"));
        course.setCourseCode(request.getCourseCode());
        course.setName(request.getName());
        course.setCredit(request.getCredit());
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    // Offering
    public List<Offering> listOfferings() {
        return offeringRepository.findAll();
    }

    @Transactional
    public Offering createOffering(OfferingRequest request) {
        Offering offering = new Offering();
        offering.setTerm(request.getTerm());
        offering.setCourse(courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new BusinessException(4040, "课程不存在")));
        offering.setTeacher(teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new BusinessException(4040, "教师不存在")));
        offering.setClassLabel(request.getClassLabel());
        offering.setWeekday(request.getWeekday());
        offering.setStartPeriod(request.getStartPeriod());
        offering.setEndPeriod(request.getEndPeriod());
        offering.setStartWeek(request.getStartWeek());
        offering.setEndWeek(request.getEndWeek());
        offering.setLocation(request.getLocation());
        offering.setCapacity(request.getCapacity());
        return offeringRepository.save(offering);
    }

    @Transactional
    public Offering updateOffering(Long id, OfferingRequest request) {
        Offering offering = offeringRepository.findById(id)
                .orElseThrow(() -> new BusinessException(4040, "开课不存在"));
        offering.setTerm(request.getTerm());
        offering.setCourse(courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new BusinessException(4040, "课程不存在")));
        offering.setTeacher(teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new BusinessException(4040, "教师不存在")));
        offering.setClassLabel(request.getClassLabel());
        offering.setWeekday(request.getWeekday());
        offering.setStartPeriod(request.getStartPeriod());
        offering.setEndPeriod(request.getEndPeriod());
        offering.setStartWeek(request.getStartWeek());
        offering.setEndWeek(request.getEndWeek());
        offering.setLocation(request.getLocation());
        offering.setCapacity(request.getCapacity());
        return offeringRepository.save(offering);
    }

    @Transactional
    public void deleteOffering(Long id) {
        offeringRepository.deleteById(id);
    }

    public List<Enrollment> listEnrollments(Long offeringId) {
        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new BusinessException(4040, "开课不存在"));
        return enrollmentRepository.findByOffering(offering);
    }

    @Transactional
    public void removeEnrollment(Long offeringId, Long studentId) {
        Offering offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new BusinessException(4040, "开课不存在"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException(4040, "学生不存在"));
        Enrollment enrollment = enrollmentRepository.findByOfferingAndStudent(offering, student)
                .orElseThrow(() -> new BusinessException(4041, "未选该课程"));
        enrollmentRepository.delete(enrollment);
    }

    // Password reset
    public List<PasswordResetRequest> pendingPasswordResets() {
        return passwordResetRequestRepository.findByStatus("PENDING");
    }

    public List<PasswordResetRequest> passwordResets(String status) {
        return passwordResetRequestRepository.findByStatus(status);
    }

    @Transactional
    public void handlePasswordReset(Long id, Long adminUserId) {
        PasswordResetRequest request = passwordResetRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(4040, "申请不存在"));
        UserRole role = request.getRole();
        String username = request.getUsername();
        UserAccount account = userAccountRepository.findByUsernameAndRole(username, role)
                .orElseThrow(() -> new BusinessException(4040, "账号不存在"));
        authService.resetPassword(account, DEFAULT_PASSWORD);
        request.setStatus("DONE");
        request.setHandledAt(java.time.Instant.now());
        request.setHandledBy(adminUserId);
        passwordResetRequestRepository.save(request);
    }

    @Transactional
    public PasswordResetRequest createPasswordResetRequest(UserRole role, String username, String name, String remark) {
        userAccountRepository.findByUsernameAndRole(username, role)
                .orElseThrow(() -> new BusinessException(4040, "账号不存在"));
        PasswordResetRequest request = new PasswordResetRequest();
        request.setRole(role);
        request.setUsername(username);
        request.setName(name);
        request.setRemark(remark);
        request.setStatus("PENDING");
        return passwordResetRequestRepository.save(request);
    }
}
