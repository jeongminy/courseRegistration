package com.teamsparta.courseregistration.domain.course.repository

import com.querydsl.core.BooleanBuilder
import com.teamsparta.courseregistration.domain.course.model.Course
import com.teamsparta.courseregistration.domain.course.model.CourseStatus
import com.teamsparta.courseregistration.domain.course.model.QCourse
import com.teamsparta.courseregistration.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import javax.swing.text.html.HTML.Tag.I


@Repository
class CourseRepositoryImpl: QueryDslSupport(), CustomCourseRepository {

    private val course = QCourse.course

    override fun searchCourseListByTitle(title: String): List<Course> {
        return queryFactory.selectFrom(course)
            .where(course.title.containsIgnoreCase(title))
            .fetch()
    }

    override fun findByPageableAndStatus(pageable: Pageable, courseStatus: CourseStatus?): Page<Course> {

        val whereClause = BooleanBuilder()
        courseStatus?.let {whereClause.and(course.status.eq(courseStatus))}

        val totalCount = queryFactory.select(course.count()).from(course).where(whereClause).fetchOne()?:0L

        val query = queryFactory.selectFrom(course)
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        if (pageable.sort.isSorted) {

            when(pageable.sort.first()?.property) {
                "id" -> query.orderBy(course.id.asc())
                "title" -> query.orderBy(course.title.asc())
                else -> query.orderBy(course.id.asc())
            }
        } else {
            query.orderBy(course.id.asc())
        }

        val contents = query.fetch()

        return PageImpl(contents, pageable, totalCount)
    }
}