package com.avinty.hr.presentation.dto;

public record RsEmployee (
    String id,
    String createdBy,
    String createdAt,
    String updatedBy,
    String updatedAt,
    String email,
    String fullName,
    String departmentId
) {

}
