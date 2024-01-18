package com.teamsparta.courseregistration.domain.course.repository

import com.teamsparta.courseregistration.domain.course.model.Course
import com.teamsparta.courseregistration.domain.course.model.QCourse
import com.teamsparta.courseregistration.infra.querydsl.QueryDslSupport
import org.springframework.stereotype.Repository

@Repository
class CourseRepositoryImpl: QueryDslSupport(), CustomCourseRepository {

    private val course = QCourse.course

    override fun searchCourseListByTitle(title: String): List<Course> {
        return queryFactiory.selectFrom(course)
            .where(course.title.containsIgnoreCase(title))
            .fetch()
    }
}