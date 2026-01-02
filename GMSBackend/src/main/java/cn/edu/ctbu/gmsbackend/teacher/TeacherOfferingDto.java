package cn.edu.ctbu.gmsbackend.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherOfferingDto {
    private Long id;
    private String term;
    private String courseName;
    private String classLabel;
    private String timePlace;
    private Integer studentCount;
}
