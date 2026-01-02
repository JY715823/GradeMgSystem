package cn.edu.ctbu.gmsbackend.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferingStudentDto {
    private Long studentId;
    private String studentNo;
    private String name;
    private String className;
    private String score;
}
