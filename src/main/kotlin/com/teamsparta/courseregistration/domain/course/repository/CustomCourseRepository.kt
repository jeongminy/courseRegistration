package com.teamsparta.courseregistration.domain.course.repository

import com.teamsparta.courseregistration.domain.course.model.Course

interface CustomCourseRepository {

    fun searchCourseListByTitle(title: String): List<Course>
}