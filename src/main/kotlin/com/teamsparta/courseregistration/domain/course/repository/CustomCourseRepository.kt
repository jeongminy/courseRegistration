package com.teamsparta.courseregistration.domain.course.repository

import com.teamsparta.courseregistration.domain.course.model.Course
import com.teamsparta.courseregistration.domain.course.model.CourseStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface CustomCourseRepository {

    fun searchCourseListByTitle(title: String): List<Course>

    fun findByPageableAndStatus(pageable: Pageable, courseStatus: CourseStatus?): Page<Course>
}