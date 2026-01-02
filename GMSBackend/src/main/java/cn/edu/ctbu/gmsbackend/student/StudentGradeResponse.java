package cn.edu.ctbu.gmsbackend.student;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StudentGradeResponse {
    private Long offeringId;
    private String courseName;
    private String teacherName;
    private BigDecimal score;
    private String status;
}
