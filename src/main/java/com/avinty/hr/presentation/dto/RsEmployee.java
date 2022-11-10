package com.avinty.hr.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RsEmployee {
    String id;
    String createdBy;
    String createdAt;
    String updatedBy;
    String updatedAt;
    String email;
    String fullName;
    String departmentId;
}
