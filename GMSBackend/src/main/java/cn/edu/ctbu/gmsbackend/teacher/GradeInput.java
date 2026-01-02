package cn.edu.ctbu.gmsbackend.teacher;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GradeInput {
    @NotNull
    private Long studentId;
    @Min(value = 0, message = "分数范围 0-100")
    @Max(value = 100, message = "分数范围 0-100")
    private BigDecimal score;
}
