package com.example.lecturer_demo.services;

import com.example.lecturer_demo.dto.CourseInstructorResponse;
import com.example.lecturer_demo.dto.CourseResponse;
import com.example.lecturer_demo.dto.CourseCreateRequest;
import com.example.lecturer_demo.dto.CourseUpdateRequest;
import com.example.lecturer_demo.models.Course;
import com.example.lecturer_demo.models.CourseStatus;
import com.example.lecturer_demo.models.Instructor;
import com.example.lecturer_demo.repositories.CourseRepository;
import com.example.lecturer_demo.repositories.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    // Thực hiện mapping từ Entity sang DTO tại Service bằng Stream API
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getStatus(),
                        new CourseInstructorResponse(
                                course.getInstructor().getId(),
                                course.getInstructor().getName()
                        )
                ))
                .toList();
    }

    public CourseResponse getCourseResponseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + id));

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getStatus(),
                new CourseInstructorResponse(
                        course.getInstructor().getId(),
                        course.getInstructor().getName()
                )
        );
    }

    public void createCourse(CourseCreateRequest req) {
        Instructor instructor = instructorRepository.findById(req.instructorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + req.instructorId()));

        Course course = new Course();
        course.setTitle(req.title());
        course.setStatus(CourseStatus.valueOf(req.status()));
        course.setInstructor(instructor);

        courseRepository.save(course);
    }

    public void updateCourse(Long id, CourseUpdateRequest req) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + id));

        Instructor instructor = instructorRepository.findById(req.instructorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + req.instructorId()));

        existing.setTitle(req.title());
        existing.setStatus(CourseStatus.valueOf(req.status()));
        existing.setInstructor(instructor);

        courseRepository.save(existing);
    }

    public void deleteCourseById(Long id) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + id));
        courseRepository.delete(existing);
    }
}