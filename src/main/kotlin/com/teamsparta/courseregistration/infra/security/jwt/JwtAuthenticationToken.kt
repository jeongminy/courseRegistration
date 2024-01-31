package com.teamsparta.courseregistration.infra.security.jwt

import com.teamsparta.courseregistration.infra.security.UserPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetails
import java.io.Serializable

//JWT 기반의 인증을 위한 커스텀 토큰으로서,
//Spring Security에서 사용자의 인증과 권한 검사를 수행하는 데에 활용된다.
class JwtAuthenticationToken(
    private val principal: UserPrincipal, //JWT 토큰에서 추출한 사용자 정보를 담고 있는 UserPrincipal 객체이다.
    details: WebAuthenticationDetails, //요청한 주소 정보와 세션 ID 등의 세부 정보를 담고 있는 WebAuthenticationDetails 객체입니다. 주로 로깅 용도로 사용됩니다.
) : AbstractAuthenticationToken(principal.authorities), Serializable {

    init { //JWT 검증이 됐을시에 바로 생성할 예정이므로, 생성시 authenticated를 true로 설정
        super.setAuthenticated(true) //인증 상태를 true로 설정
        super.setDetails(details) //인증에 필요한 세부 정보를 설정
    }

    override fun getPrincipal() = principal //사용자 정보를 반환하는 메서드입니다. 이 경우, principal 프로퍼티를 반환합니다.

    override fun getCredentials() = null //자격 증명 정보를 반환하는 메서드입니다. 이 경우, JWT 토큰에는 자격 증명 정보가 없으므로 null을 반환합니다.

    override fun isAuthenticated(): Boolean { //인증 상태를 반환하는 메서드입니다. 이 경우, 항상 true를 반환하도록 재정의되어 있습니다.
        return true
    }

}