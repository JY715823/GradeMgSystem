package cn.edu.ctbu.gmsbackend.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseRequest {
    @NotBlank
    private String courseCode;
    @NotBlank
    private String name;
    private BigDecimal credit;
}
