package com.teamsparta.courseregistration.domain.course.controller

import com.teamsparta.courseregistration.domain.course.dto.CourseResponse
import com.teamsparta.courseregistration.domain.course.dto.CreateCourseRequest
import com.teamsparta.courseregistration.domain.course.dto.UpdateCourseRequest
import com.teamsparta.courseregistration.domain.course.service.CourseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/courses") // "/courses" 경로로 들어오는 모든 요청을 이 컨트롤러가 처리한다. // Handler Mapping에게 어떤 url을 담당하는지 알려줘야 해.
@RestController // @RestController 어노테이션으로 이 클래스가 REST API 컨트롤러임을 선언합니다.
class CourseController(
    private val courseService: CourseService // CourseService 인터페이스를 생성자를 통해 주입받습니다.
) {

    @GetMapping("/search")
    @PreAuthorize("hasRole('TUTOR') or hasRole('STUDENT')")
    fun searchCourseList(@RequestParam(value = "title") title: String): ResponseEntity<List<CourseResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.searchCourseList(title))
    }

//    //pageable을 붙이기만 해도 page, size, sort가 자동 부여됨
//    @GetMapping
//    @PreAuthorize("hasRole('TUTOR') or hasRole('STUDENT')")
//    fun getCourseList(pageable:Pageable): ResponseEntity<List<CourseResponse>> {
//        return ResponseEntity
//            .status(HttpStatus.OK) // Get의 경우 성공하면 200을 리턴
//            .body(courseService.getAllCourseList())
//    }


//    //value를 사용해서 구현할 수 도 있어 (이런 방식도 있음)
//    @GetMapping
//    @PreAuthorize("hasRole('TUTOR') or hasRole('STUDENT')")
//    fun getCourseList(
//        @RequestParam(value = "page") page: Int,
//        @RequestParam(value = "size") size: Int,
//        @RequestParam(value = "orderby") orderby: String
//    ): ResponseEntity<List<CourseResponse>> {
//
//        PageRequest.of()
//
//        return ResponseEntity
//            .status(HttpStatus.OK)  // Get의 경우 성공하면 200을 리턴
//            .body(CourseService.getAllCourseList()) // getAllCourseList()함수는 List<CourseResponse>를 리턴함. -> body로 DTO가 담김
//    }



    @GetMapping
    @PreAuthorize("hasRole('TUTOR') or hasRole('STUDENT')")
    fun getCourseList(
        @PageableDefault(
            size=15,
            sort = ["id"]
        ) pageable:Pageable, //pageable은 페이지 정보를 담고있는 하나의 객체 이다.
        @RequestParam(value = "status", required = false) status: String?
    ): ResponseEntity<Page<CourseResponse>> {

//        assert(pageable.sort.first()?.property == "id") //첫 번째 정렬(sort) 기준이 "id"인지를 검증하는 것을 의미
//        assert(pageable.sort.isSorted) //정렬 기준이 있는지 확인하고 싶다면 isSorted 이용

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getPaginatedCourseList(pageable, status))
//            .body(courseService.getAllCourseList())
    }

    @PreAuthorize("hasRole('TUTOR') or hasRole('STUDENT')")
    @GetMapping("/{courseId}") // 만약 위에 RequestMapping이 되어있지 않다면 /courses/{courseId} 로 입력해주어야함
    fun getCourse(@PathVariable courseId: Long): ResponseEntity<CourseResponse> { // 보통 메소드의 인자를 getMapping의 인자와 일치해줌
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getCourseById(courseId))
    }

    @PreAuthorize("hasRole('TUTOR')")
    @PostMapping
    fun createCourse(@RequestBody createCourseRequest: CreateCourseRequest): ResponseEntity<CourseResponse> { // CreatCourseRequest로 Jason을 맵핑해야 하기 때문에
        return ResponseEntity
            .status(HttpStatus.CREATED) // CREATED 는 201
            .body(courseService.createCourse(createCourseRequest))
    }

    @PreAuthorize("hasRole('TUTOR')")
    @PutMapping("/{courseId}")
    fun updateCourse(
        @PathVariable courseId: Long,
        @RequestBody updateCourseRequest: UpdateCourseRequest
    ): ResponseEntity<CourseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.updateCourse(courseId, updateCourseRequest))
    }

    @PreAuthorize("hasRole('TUTOR')")
    @DeleteMapping("/{courseId}")
    fun deleteCourse(@PathVariable courseId: Long): ResponseEntity<Unit> { // Unit은 아무것도 리턴하지 않는다.
        courseService.deleteCourse(courseId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT) // 상태 204를 리턴
            .build() // body를 아무것도 표현하지 않을때에는 그냥 build() 를 호출하면 됨.
    }
}