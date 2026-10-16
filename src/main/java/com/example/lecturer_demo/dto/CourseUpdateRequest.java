package com.example.lecturer_demo.dto;


public record CourseUpdateRequest(
        String title,
        String status,
        Long instructorId
) {}
