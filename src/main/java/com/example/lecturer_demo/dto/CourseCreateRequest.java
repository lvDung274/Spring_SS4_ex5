package com.example.lecturer_demo.dto;


public record CourseCreateRequest(
        String title,
        String status,
        Long instructorId
) {}