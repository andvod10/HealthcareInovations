package com.avinty.hr.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RqEditEmployee {
    String id;
    String createdBy;
    String updatedBy;
    String email;
    String password;
    String fullName;
    String departmentId;
}
