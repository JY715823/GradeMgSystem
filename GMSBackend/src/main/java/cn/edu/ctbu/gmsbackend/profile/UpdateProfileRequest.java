package cn.edu.ctbu.gmsbackend.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    @NotBlank(message = "请输入姓名")
    private String name;
    private String classOrDept;
    private String phone;
    private String email;
}
