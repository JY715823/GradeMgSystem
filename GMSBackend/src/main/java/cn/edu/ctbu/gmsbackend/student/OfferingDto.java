package cn.edu.ctbu.gmsbackend.student;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferingDto {
    private Long id;
    private String term;
    private String courseName;
    private String teacherName;
    private String classLabel;
    private Integer weekday;
    private Integer startPeriod;
    private Integer endPeriod;
    private Integer startWeek;
    private Integer endWeek;
    private String location;
    private Integer capacity;
    private Integer selectedCount;
}
