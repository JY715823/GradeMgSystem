package cn.edu.ctbu.gmsbackend.profile;

import cn.edu.ctbu.gmsbackend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ApiResponse<ProfileResponse> me() {
        return ApiResponse.ok(profileService.getProfile());
    }

    @PutMapping("/me")
    public ApiResponse<ProfileResponse> update(@Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.ok(profileService.updateProfile(request));
    }

    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(request);
        return ApiResponse.ok();
    }
}
