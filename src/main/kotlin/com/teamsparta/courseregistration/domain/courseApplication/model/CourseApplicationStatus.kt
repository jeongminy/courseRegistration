package com.teamsparta.courseregistration.domain.courseApplication.model

enum class CourseApplicationStatus {
    PENDING, // 승인 및 거절 전
    ACCEPTED, // 승인됨
    REJECTED // 거절됨
}