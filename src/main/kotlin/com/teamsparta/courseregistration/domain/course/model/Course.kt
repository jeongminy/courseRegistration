package com.teamsparta.courseregistration.domain.course.model

import com.teamsparta.courseregistration.domain.course.dto.CourseResponse
import com.teamsparta.courseregistration.domain.courseApplication.model.CourseApplication
import com.teamsparta.courseregistration.domain.lecture.model.Lecture
import jakarta.persistence.*

@Entity // Table과 매핑되는 객체다 !! 하고 JPA에게 알려줌
@Table(name = "course")
class Course(
    @Column(name = "title", nullable = false)  // 이제 데이터를 정의를 해야해 (Column정의)
    var title: String,

    @Column(name = "description")
    var description: String? = null, //설명은 optional(존재할수도, 존재하지않을수도) 할 수 있기 때문에 nullable하게 정함.

    @Enumerated(EnumType.STRING) // enum을 사용하기위해 @Enumerated 어노테이션 사용
    @Column(name = "status", nullable = false)

    var status: CourseStatus = CourseStatus.OPEN,
    // 코스가 오픈인지 마감인지 // 하지만 open close가 아닌 다른값이 들어갈 수 있으므로 String이 아닌 enum으로 설정하는게 좋음.
    // enum (CourseStatus)에는 open과 closed만 있다.
    // DB에는 open은 0 , closed는 1 이 저장됨


    //최대 신청 가능 인원 설정
    @Column(name = "max_applicants", nullable = false)
    val maxApplicants: Int = 30,

    @Column(name = "num_applicants", nullable = false)
    var numApplicants: Int = 0,

    // 1:N관계
    // mappedBy는 보통 FK를 들고있지 않은 쪽(1)에 설정함
    // fetch는 lazy로 지연로딩으로 설정함.
    // casecade는 부모자식관계로 생명주기를 따라가게함. 따라서 lecture와 생명주기를 같이함.
    // 'orphanRemoval = true' 는 부모객체가 삭제될때 '고아자식객체'가 된 자식객체도 함께 삭제되도록 함.
    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var lectures: MutableList<Lecture> = mutableListOf(),

    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var courseApplications: MutableList<CourseApplication> = mutableListOf()
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 데이터베이스에 아이디를 위임해서 자동생성 한다는 뜻 // 한마디로 데이터베이스에 갔다 와야 아이디가 생성될 수 있음.

    var id: Long? = null // id는 nullable 하면 안되는데 nullable 하게 한 이유는? -> Database에 위임을 맡길 거기 때문.


    fun isFull(): Boolean {
        return numApplicants >= maxApplicants
    }

    fun isClosed(): Boolean {
        return status == CourseStatus.CLOSED
    }

    fun close() {
        status = CourseStatus.CLOSED
    }

    fun addApplicant() {
        numApplicants += 1
    }

    fun addLecture(lecture: Lecture) {
        lectures.add(lecture)
    }

    fun removeLecture(lecture: Lecture) {
        lectures.remove(lecture)
    }

    fun addCourseApplication(courseApplication: CourseApplication) {
        courseApplications.add(courseApplication)
    }

}

fun Course.toResponse(): CourseResponse {
    return CourseResponse(
        id = id!!,
        title = title,
        description = description,
        status = status.name,
        maxApplicants = maxApplicants,
        numApplicants = numApplicants
    )
}