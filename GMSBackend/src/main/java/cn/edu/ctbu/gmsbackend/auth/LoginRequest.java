package cn.edu.ctbu.gmsbackend.auth;

import cn.edu.ctbu.gmsbackend.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotNull
    private UserRole role;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
