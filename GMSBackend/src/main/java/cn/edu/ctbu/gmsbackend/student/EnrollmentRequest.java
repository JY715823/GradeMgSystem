package cn.edu.ctbu.gmsbackend.student;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentRequest {
    @NotNull
    private Long offeringId;
}
