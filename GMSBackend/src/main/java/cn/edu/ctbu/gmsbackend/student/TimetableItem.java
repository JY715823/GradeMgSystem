package cn.edu.ctbu.gmsbackend.student;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimetableItem {
    private Long offeringId;
    private String courseName;
    private String teacherName;
    private Integer weekday;
    private Integer startPeriod;
    private Integer endPeriod;
    private Integer startWeek;
    private Integer endWeek;
    private String location;
}
