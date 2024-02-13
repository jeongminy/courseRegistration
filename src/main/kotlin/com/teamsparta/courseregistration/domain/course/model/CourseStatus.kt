package com.teamsparta.courseregistration.domain.course.model

import com.fasterxml.jackson.annotation.JsonCreator
import org.apache.commons.lang3.EnumUtils

enum class CourseStatus {
    OPEN, //0
    CLOSED; //1


        //CourseStatus 열거형을 문자열로 파싱하는 기능을 제공하는 메서드를 정의하고 있습니다.
        //열거형의 값을 문자열로 표현하고자 할 때, 해당 메서드를 사용하여 문자열을 열거형 값으로 변환할 수 있습니다.
        //얘를 코드로 작성해주면, Controller에서 바로 써도 된다고 함. (어떻게 실제로 활용되는지는 모루겠담..)
        companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun parse(name: String?): CourseStatus? =
            name?.let { EnumUtils.getEnumIgnoreCase(CourseStatus::class.java, it.trim())}
    }
}