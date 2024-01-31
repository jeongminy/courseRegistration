package com.teamsparta.courseregistration.infra.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal( //인증된 사용자의 정보를 담는 클래스로, 주로 인증 후에 사용자의 정보를 저장하고 전달하는 데 사용됩니다.
    val id: Long, //사용자의 고유 식별자
    val email: String, //사용자의 이메일 주소
    val authorities: Collection<GrantedAuthority> //사용자의 권한
){
    //보조 생성자로 오버로딩된 생성자를 하나 더 정의함 //보조 생성자는 주 생성자의 일부 기능을 대체하거나 보완하는 역할
    constructor(id: Long, email: String, roles: Set<String>): this(
        id,
        email,
        roles.map {SimpleGrantedAuthority("ROLE_$it")} // "ROLE 후 '언더바'를 넣는게 좋음
    )
}