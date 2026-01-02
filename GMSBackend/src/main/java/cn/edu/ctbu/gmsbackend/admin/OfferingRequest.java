package cn.edu.ctbu.gmsbackend.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferingRequest {
    @NotNull
    private String term;
    @NotNull
    private Long courseId;
    @NotNull
    private Long teacherId;
    private String classLabel;
    @NotNull
    private Integer weekday;
    @NotNull
    private Integer startPeriod;
    @NotNull
    private Integer endPeriod;
    @NotNull
    private Integer startWeek;
    @NotNull
    private Integer endWeek;
    private String location;
    @NotNull
    private Integer capacity;
}
