package com.teamsparta.courseregistration.domain.user.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Profile (

    @Column(name = "nickname")
    var nickname: String, // 바뀔 수 있으니 var
)