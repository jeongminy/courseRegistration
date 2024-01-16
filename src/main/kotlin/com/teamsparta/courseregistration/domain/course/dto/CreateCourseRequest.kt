package com.teamsparta.courseregistration.domain.course.dto

data class CreateCourseRequest (
    val title: String,
    val description: String?,
)