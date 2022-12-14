package com.avinty.hr.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class RqEmployee {
    String createdBy;
    String updatedBy;
    @NotEmpty
    String email;
    @NotEmpty
    String password;
    String fullName;
    String departmentId;
}
