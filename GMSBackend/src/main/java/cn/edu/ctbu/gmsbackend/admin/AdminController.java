package cn.edu.ctbu.gmsbackend.admin;

import cn.edu.ctbu.gmsbackend.common.ApiResponse;
import cn.edu.ctbu.gmsbackend.common.BusinessException;
import cn.edu.ctbu.gmsbackend.entity.*;
import cn.edu.ctbu.gmsbackend.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private Long currentAdminId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new BusinessException(4010, "未登录");
        }
        UserAccount user = userDetails.getUser();
        if (user.getRole() != UserRole.ADMIN) {
            throw new BusinessException(4030, "仅管理员可访问");
        }
        return user.getId();
    }

    // students
    @GetMapping("/students")
    public ApiResponse<List<Student>> students() {
        return ApiResponse.ok(adminService.listStudents());
    }

    @PostMapping("/students")
    public ApiResponse<Student> createStudent(@Valid @RequestBody StudentRequest request) {
        return ApiResponse.ok(adminService.createStudent(request));
    }

    @PutMapping("/students/{id}")
    public ApiResponse<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return ApiResponse.ok(adminService.updateStudent(id, request));
    }

    @DeleteMapping("/students/{id}")
    public ApiResponse<Void> deleteStudent(@PathVariable Long id) {
        adminService.deleteStudent(id);
        return ApiResponse.ok();
    }

    @PostMapping("/students/{id}/reset-password")
    public ApiResponse<Void> resetStudentPassword(@PathVariable Long id) {
        adminService.resetStudentPassword(id);
        return ApiResponse.ok();
    }

    // teachers
    @GetMapping("/teachers")
    public ApiResponse<List<Teacher>> teachers() {
        return ApiResponse.ok(adminService.listTeachers());
    }

    @PostMapping("/teachers")
    public ApiResponse<Teacher> createTeacher(@Valid @RequestBody TeacherRequest request) {
        return ApiResponse.ok(adminService.createTeacher(request));
    }

    @PutMapping("/teachers/{id}")
    public ApiResponse<Teacher> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherRequest request) {
        return ApiResponse.ok(adminService.updateTeacher(id, request));
    }

    @DeleteMapping("/teachers/{id}")
    public ApiResponse<Void> deleteTeacher(@PathVariable Long id) {
        adminService.deleteTeacher(id);
        return ApiResponse.ok();
    }

    @PostMapping("/teachers/{id}/reset-password")
    public ApiResponse<Void> resetTeacherPassword(@PathVariable Long id) {
        adminService.resetTeacherPassword(id);
        return ApiResponse.ok();
    }

    // courses
    @GetMapping("/courses")
    public ApiResponse<List<Course>> courses() {
        return ApiResponse.ok(adminService.listCourses());
    }

    @PostMapping("/courses")
    public ApiResponse<Course> createCourse(@Valid @RequestBody CourseRequest request) {
        return ApiResponse.ok(adminService.createCourse(request));
    }

    @PutMapping("/courses/{id}")
    public ApiResponse<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        return ApiResponse.ok(adminService.updateCourse(id, request));
    }

    @DeleteMapping("/courses/{id}")
    public ApiResponse<Void> deleteCourse(@PathVariable Long id) {
        adminService.deleteCourse(id);
        return ApiResponse.ok();
    }

    // offerings
    @GetMapping("/offerings")
    public ApiResponse<List<Offering>> offerings() {
        return ApiResponse.ok(adminService.listOfferings());
    }

    @PostMapping("/offerings")
    public ApiResponse<Offering> createOffering(@Valid @RequestBody OfferingRequest request) {
        return ApiResponse.ok(adminService.createOffering(request));
    }

    @PutMapping("/offerings/{id}")
    public ApiResponse<Offering> updateOffering(@PathVariable Long id, @Valid @RequestBody OfferingRequest request) {
        return ApiResponse.ok(adminService.updateOffering(id, request));
    }

    @DeleteMapping("/offerings/{id}")
    public ApiResponse<Void> deleteOffering(@PathVariable Long id) {
        adminService.deleteOffering(id);
        return ApiResponse.ok();
    }

    // enrollments
    @GetMapping("/offerings/{offeringId}/enrollments")
    public ApiResponse<List<Enrollment>> enrollmentList(@PathVariable Long offeringId) {
        return ApiResponse.ok(adminService.listEnrollments(offeringId));
    }

    @DeleteMapping("/offerings/{offeringId}/enrollments/{studentId}")
    public ApiResponse<Void> deleteEnrollment(@PathVariable Long offeringId, @PathVariable Long studentId) {
        adminService.removeEnrollment(offeringId, studentId);
        return ApiResponse.ok();
    }

    // password reset requests
    @GetMapping("/password-resets")
    public ApiResponse<List<PasswordResetRequest>> passwordResets(@RequestParam(defaultValue = "PENDING") String status) {
        return ApiResponse.ok(adminService.passwordResets(status.toUpperCase()));
    }

    @PostMapping("/password-resets/{id}/handle")
    public ApiResponse<Void> handleReset(@PathVariable Long id, @Valid @RequestBody PasswordResetHandleRequest request) {
        adminService.handlePasswordReset(id, request.getNewPassword(), currentAdminId());
        return ApiResponse.ok();
    }
}
