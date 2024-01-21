package com.teamsparta.courseregistration.domain.course.model

import com.fasterxml.jackson.annotation.JsonCreator
import org.apache.commons.lang3.EnumUtils

enum class CourseStatus {
    OPEN, //0
    CLOSED; //1


    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun pase(name: String?): CourseStatus? =
            name?.let { EnumUtils.getEnumIgnoreCase(CourseStatus::class.java, it.trim())}
    }
}