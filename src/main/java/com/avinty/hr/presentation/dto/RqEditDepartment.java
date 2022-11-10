package com.avinty.hr.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RqEditDepartment {
    String id;
    String createdBy;
    String updatedBy;
    String name;
    String managerId;
}
