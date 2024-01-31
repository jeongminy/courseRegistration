package com.teamsparta.courseregistration.infra.security.jwt

import com.teamsparta.courseregistration.infra.security.UserPrincipal
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
//JWT 토큰의 유효성을 검증한다. //검증에 성공한 경우 해당 토큰의 내용을 사용하여 인증 객체를 생성하고 SecurityContext에 저장하는 역할을 한다.
class JwtAuthenticationFilter(
    private val jwtPlugin: JwtPlugin
) : OncePerRequestFilter() { //OncePerRequestFilter()필터를 상속받는다.

    companion object {
        private val BEARER_PATTERN = Regex("^Bearer (.+?)$")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = request.getBearerToken() //추출한 Bearer토큰을 jwt변수에 할당한다.

        //jwt가 null일 수 있기 때문에, null이 아닐 경우에 검증을 한다. (즉, Bearer 토큰이 존재하는 경우에만 JWT 토큰의 유효성을 검증하고 처리)
        if (jwt != null) {
            jwtPlugin.validateToken(jwt)
                //jwtPlugin 객체를 사용하여 JWT 토큰의 유효성을 검증하는 메서드를 호출
                //메서드는 비동기로 처리되며, 검증에 성공한 경우 onSuccess 람다 함수가 호출됩니다.
                .onSuccess {//onSuccess 람다 함수에서는 validateToken() 메서드의 결과로 전달된 it 객체를 사용하여 JWT 토큰의 내용을 추출합니다
                    val userId = it.payload.subject.toLong() //토큰의 subject 값을 가져오고, 이 값을 userId 변수에 Long 타입으로 변환하여 저장합니다.
                    val role = it.payload.get("role", String::class.java) //토큰의 role 값을 가져오고, role 변수에 저장한다.
                    val email = it.payload.get("email", String::class.java) //토큰의 email 값을 가져오고, email 변수에 저장한다.

                    val principal = UserPrincipal( //UserPrincipal 객체를 생성하여 userId, email, role 값을 사용하여 초기화합니다.
                        id = userId,
                        email = email,
                        roles = setOf(role)
                    )
                    // Authentication 구현체 생성
                    val authentication = JwtAuthenticationToken( //JwtAuthenticationToken 객체를 생성
                        principal = principal, //principal에는 이전에 생성한 UserPrincipal 객체를 생성하여 할당함
                        details =  WebAuthenticationDetailsSource().buildDetails(request) // request로 부터 상세정보들을 넣어준다.
                        //Spring Security에서 제공하는 WebAuthenticationDetailsSource 클래스의 buildDetails 메서드를 호출하는 코드이다.
                        //현재 사용자의 인증 요청에 대한 상세 정보를 생성하는 코드로,
                        // HttpServletRequest 객체를 이용하여 사용자의 IP 주소, 세션 ID, 요청한 URI 등의 정보를 추출합니다.
                    )


                    // SecurityContext에 authentication 객체 저장
                    SecurityContextHolder.getContext().authentication = authentication
                }
                //사실 여기에 detail하게 실무로 갈때는 jwt가 실패했을 때, 왜 실패했는지 처리를 해주어야함(만료가 됬다든지 등등), 하지만 여기선 따로 처리하지 않겠음
//                .onFailure {  }
        }

        filterChain.doFilter(request, response) //잊지마!! 다음 필터로 요청을 전달해야됨
    }

    //HttpServletRequest 객체에서 Authorization 헤더에서 Bearer 토큰을 추출하는 함수
    private fun HttpServletRequest.getBearerToken(): String? { //HttpServletRequest로 부터 Token을 가져온다. // 없을 수도 있으니 nullable 함.
        val headerValue = this.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        //getBearerToken() 함수는 HttpServletRequest 객체의 getHeader() 메서드를 사용하여 Authorization 헤더 값을 가져옵니다.
        //만약 Authorization 헤더 값이 null이라면, 즉 헤더가 존재하지 않는다면 null을 반환합니다.

        return BEARER_PATTERN.find(headerValue)?.groupValues?.get(1)
        //BEARER_PATTERN은 "^Bearer (.+?)$" 정규식을 나타내며, Bearer 다음에 공백을 포함한 그룹을 추출하도록 설정되어 있습니다.
        //BEARER_PATTERN.find(headerValue)를 사용하여 정규식과 일치하는 첫 번째 매칭을 찾고,
        // groupValues를 사용하여 매칭된 그룹의 값을 가져옵니다.
        //여기서 1번 인덱스의 값은 Bearer 다음에 오는 토큰 값입니다.

        //따라서, getBearerToken() 함수는 Bearer 토큰 값을 반환하거나,
        //Authorization 헤더가 없거나 Bearer 토큰이 없을 경우 null을 반환합니다.
        //이를 통해 JWT 토큰을 추출하는 기능을 구현할 수 있습니다.
    }
}