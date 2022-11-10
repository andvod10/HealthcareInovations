package com.avinty.hr.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RqChangeDepartment {
    String updatedBy;
    String departmentId;
    String employeeId;
}
