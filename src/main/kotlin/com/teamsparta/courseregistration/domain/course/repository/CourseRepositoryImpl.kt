package com.teamsparta.courseregistration.domain.course.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import com.teamsparta.courseregistration.domain.course.model.Course
import com.teamsparta.courseregistration.domain.course.model.CourseStatus
import com.teamsparta.courseregistration.domain.course.model.QCourse
import com.teamsparta.courseregistration.domain.lecture.model.QLecture
import com.teamsparta.courseregistration.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository



@Repository //외부기능을 활용하다 보니 Repository 어노테이션을 붙여 주어야 함. 그래야 spring Bean으로서 동작함.
class CourseRepositoryImpl: QueryDslSupport(), CustomCourseRepository {

    private val course = QCourse.course //course 에 대한 QClass를 멤버 변수로 선언 해준다.

    //title 기준으로 course를 조회함
    override fun searchCourseListByTitle(title: String): List<Course> {
        return queryFactory.selectFrom(course)
            .where(course.title.containsIgnoreCase(title)) //containsIgnoreCase 는 대소문자 구분 하지 않고 검색해 줘서 좋음.
            .fetch()
    }

    override fun findByPageableAndStatus(pageable: Pageable, courseStatus: CourseStatus?): Page<Course> {

        val whereClause = BooleanBuilder()
        courseStatus?.let {whereClause.and(course.status.eq(courseStatus))}

        val totalCount = queryFactory.select(course.count()).from(course).where(whereClause).fetchOne() ?: 0L

//        val query = queryFactory.selectFrom(course)
//            .where(whereClause)
//            .offset(pageable.offset)
//            .limit(pageable.pageSize.toLong())
//
//        if (pageable.sort.isSorted) {
//
//            when(pageable.sort.first()?.property) {
//                "id" -> query.orderBy(course.id.asc())
//                "title" -> query.orderBy(course.title.asc())
//                else -> query.orderBy(course.id.asc())
//            }
//        } else {
//            query.orderBy(course.id.asc())
//        }

        val lecture = QLecture.lecture
        val contents = queryFactory.selectFrom(course)
            .where(whereClause)
            .leftJoin(course.lectures, lecture) //성능 최적화를 위해 fetch join
            .fetchJoin() //성능 최적화를 위해 fetch join
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, course))
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }

    private fun getOrderSpecifier(pageable: Pageable, path: EntityPathBase<*>): Array<OrderSpecifier<*>> {
        val pathBuilder = PathBuilder(path.type, path.metadata)

        return pageable.sort.toList().map {
            order -> OrderSpecifier(
                if (order.isAscending) Order.ASC else Order.DESC,
                pathBuilder.get(order.property) as Expression<Comparable<*>>
            )
        }.toTypedArray()
    }






}