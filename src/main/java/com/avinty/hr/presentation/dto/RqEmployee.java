package com.avinty.hr.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class RqEmployee {
    @NotEmpty
    String email;
    @NotEmpty
    String password;
    String fullName;
    String departmentId;
}
