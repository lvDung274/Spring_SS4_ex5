package com.example.lecturer_demo.services;

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

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));
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
        Course existing = getCourseById(id);

        Instructor instructor = instructorRepository.findById(req.instructorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + req.instructorId()));

        existing.setTitle(req.title());
        existing.setStatus(CourseStatus.valueOf(req.status()));
        existing.setInstructor(instructor);

        courseRepository.save(existing);
    }

    public void deleteCourseById(Long id) {
        Course existing = getCourseById(id);
        courseRepository.delete(existing);
    }
}