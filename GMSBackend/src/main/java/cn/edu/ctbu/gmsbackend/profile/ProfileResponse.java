package cn.edu.ctbu.gmsbackend.profile;

import cn.edu.ctbu.gmsbackend.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private Long userId;
    private UserRole role;
    private String username;
    private String name;
    private String classOrDept;
    private String phone;
    private String email;
}
