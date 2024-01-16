package com.teamsparta.courseregistration.domain.courseApplication.controller

import com.teamsparta.courseregistration.domain.course.service.CourseService
import com.teamsparta.courseregistration.domain.courseApplication.dto.ApplyCourseRequest
import com.teamsparta.courseregistration.domain.courseApplication.dto.CourseApplicationResponse
import com.teamsparta.courseregistration.domain.courseApplication.dto.UpdateApplicationStatusRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RequestMapping("/courses/{courseId}/applications")
@RestController
class CourseApplicationController(
    private val courseService: CourseService
){

    @PostMapping
    fun applyCourse(@PathVariable courseId: Long, applyCourseRequest: ApplyCourseRequest): ResponseEntity<CourseApplicationResponse> {
        TODO("Not yet implemented")
    }

    @GetMapping()
    fun getApplicationList(@PathVariable courseId: Long): ResponseEntity<List<CourseApplicationResponse>> {
        TODO("Not yet implemented")
    }

    @GetMapping("/{applicationId}")
    fun getApplication(
        @PathVariable courseId: Long,
        @PathVariable applicationId: Long
    ): ResponseEntity<CourseApplicationResponse> {
        TODO("Not yet implemented")
    }

    @PatchMapping("/{applicationId}")
    fun updateApplicationStatus(
        @PathVariable courseId: Long,
        @PathVariable applicationId: Long,
        @RequestBody updateApplicationStatusRequest: UpdateApplicationStatusRequest
    ): ResponseEntity<CourseApplicationResponse> {
        TODO("Not yet implemented")
    }

}

// CourseApplicationController.kt