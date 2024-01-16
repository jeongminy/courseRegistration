package com.teamsparta.courseregistration.domain.user.model

import com.teamsparta.courseregistration.domain.courseapplication.model.CourseApplication
import com.teamsparta.courseregistration.domain.user.dto.UserResponse
import jakarta.persistence.*

@Entity
@Table(name = "app_user")
class User (

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Embedded // @Embeddable과 @Embedded는 한 세트임
    var profile: Profile,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val courseApplications: MutableList<CourseApplication> = mutableListOf()

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스에 아이디를 위임해서 자동생성 한다는 뜻 // 한마디로 데이터베이스에 갔다 와야 아이디가 생성될 수 있음.
    var id: Long? = null // id는 nullable 하면 안되는데 nullable 하게 한 이유는? -> Database에 위임을 맡길 거기 때문.
}

fun User.toResponse(): UserResponse {
    return UserResponse(
        id = id!!,
        nickname = profile.nickname,
        email = email,
        role = role.name
    )
}