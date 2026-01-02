package cn.edu.ctbu.gmsbackend.admin;

import cn.edu.ctbu.gmsbackend.common.ApiResponse;
import cn.edu.ctbu.gmsbackend.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password-resets")
@RequiredArgsConstructor
@Validated
public class PasswordResetPublicController {

    private final AdminService adminService;

    @PostMapping("/request")
    public ApiResponse<Void> requestReset(@RequestBody @Validated ResetRequestBody body) {
        adminService.createPasswordResetRequest(body.getRole(), body.getUsername(), body.getName(), body.getRemark());
        return ApiResponse.ok();
    }

    @Getter
    @Setter
    public static class ResetRequestBody {
        @NotNull
        private UserRole role;
        @NotBlank
        private String username;
        @NotBlank
        private String name;
        private String remark;
    }
}
