package cn.edu.ctbu.gmsbackend.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetHandleRequest {
    @NotBlank
    private String newPassword;
}
