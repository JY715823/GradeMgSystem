package cn.edu.ctbu.gmsbackend.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequest {
    @NotBlank
    private String studentNo;
    @NotBlank
    private String name;
    @NotBlank
    private String className;
    private String phone;
    private String email;
}
