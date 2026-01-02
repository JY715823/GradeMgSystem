package cn.edu.ctbu.gmsbackend.student;

import cn.edu.ctbu.gmsbackend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/offerings")
    public ApiResponse<List<OfferingDto>> offerings(@RequestParam(required = false) String term,
                                                    @RequestParam(required = false) String keyword) {
        return ApiResponse.ok(studentService.listOfferings(term, keyword));
    }

    @PostMapping("/enrollments")
    public ApiResponse<Void> enroll(@Valid @RequestBody EnrollmentRequest request) {
        studentService.enroll(request.getOfferingId());
        return ApiResponse.ok();
    }

    @DeleteMapping("/enrollments/{offeringId}")
    public ApiResponse<Void> drop(@PathVariable Long offeringId) {
        studentService.drop(offeringId);
        return ApiResponse.ok();
    }

    @GetMapping("/timetable")
    public ApiResponse<List<TimetableItem>> timetable(@RequestParam String term) {
        return ApiResponse.ok(studentService.timetable(term));
    }

    @GetMapping("/grades")
    public ApiResponse<List<StudentGradeResponse>> grades(@RequestParam String term) {
        return ApiResponse.ok(studentService.grades(term));
    }
}
