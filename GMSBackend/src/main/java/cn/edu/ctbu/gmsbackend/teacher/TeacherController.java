package cn.edu.ctbu.gmsbackend.teacher;

import cn.edu.ctbu.gmsbackend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/offerings")
    public ApiResponse<List<TeacherOfferingDto>> myOfferings(@RequestParam(required = false) String term) {
        return ApiResponse.ok(teacherService.myOfferings(term));
    }

    @GetMapping("/offerings/{offeringId}/students")
    public ApiResponse<List<OfferingStudentDto>> students(@PathVariable Long offeringId) {
        return ApiResponse.ok(teacherService.offeringStudents(offeringId));
    }

    @PutMapping("/offerings/{offeringId}/grades/draft")
    public ApiResponse<Void> draft(@PathVariable Long offeringId, @Valid @RequestBody List<GradeInput> inputs) {
        teacherService.saveDraft(offeringId, inputs);
        return ApiResponse.ok();
    }

    @PostMapping("/offerings/{offeringId}/grades/publish")
    public ApiResponse<Void> publish(@PathVariable Long offeringId) {
        teacherService.publish(offeringId);
        return ApiResponse.ok();
    }
}
