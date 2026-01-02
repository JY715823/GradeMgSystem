package cn.edu.ctbu.gmsbackend.auth;

import cn.edu.ctbu.gmsbackend.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long userId;
    private UserRole role;
    private String username;
}
