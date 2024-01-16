package com.teamsparta.courseregistration.domain.lecture.model

import com.teamsparta.courseregistration.domain.course.model.Course
import com.teamsparta.courseregistration.domain.lecture.dto.LectureResponse
import jakarta.persistence.*

@Entity
@Table(name = "lecture")
class Lecture (

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "video_url", nullable = false)
    var videoUrl: String,

    @ManyToOne(fetch = FetchType.LAZY) // N:1관계
    @JoinColumn(name = "course_id")
    val course: Course
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스에 아이디를 위임해서 자동생성 한다는 뜻 // 한마디로 데이터베이스에 갔다 와야 아이디가 생성될 수 있음.
    var id: Long? = null // id는 nullable 하면 안되는데 nullable 하게 한 이유는? -> Database에 위임을 맡길 거기 때문.
}

fun Lecture.toResponse(): LectureResponse {
    return LectureResponse(
        id = id!!,
        title = title,
        videoUrl = videoUrl,
    )
}