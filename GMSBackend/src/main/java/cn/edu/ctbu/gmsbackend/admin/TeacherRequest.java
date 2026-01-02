package cn.edu.ctbu.gmsbackend.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherRequest {
    @NotBlank
    private String teacherNo;
    @NotBlank
    private String name;
    private String dept;
    private String phone;
    private String email;
}
