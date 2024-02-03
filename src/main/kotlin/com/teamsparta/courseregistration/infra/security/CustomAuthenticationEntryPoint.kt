package com.teamsparta.courseregistration.infra.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamsparta.courseregistration.domain.exception.dto.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
//이 코드는 JWT 검증에 실패한 경우에 호출되는 인증 진입점으로,
//클라이언트에게 401 Unauthorized 상태 코드와 함께 JSON 형식의 에러 응답을 반환합니다.
//이를 통해 클라이언트는 JWT의 검증 실패를 알 수 있습니다.
class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {
    override fun commence( //인증에 실패한 경우 호출되는 메서드입니다.
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {

        //response객체를 사용하여 응답에 대한 설정을 합니다.
        response.status = HttpServletResponse.SC_UNAUTHORIZED //401 Unauthorized 상태 코드를 반환
        response.contentType = MediaType.APPLICATION_JSON_VALUE //응답의 컨텐츠 타입을 JSON으로 지정합니다.
        response.characterEncoding = "UTF-8" //문자 인코딩을 UTF-8로 지정합니다.

        //에러 응답 생성
        val objectMapper = ObjectMapper() //ObjectMapper를 사용하여 ErrorResponse 객체를 JSON 형식의 문자열로 변환합니다.
        val jsonString = objectMapper.writeValueAsString(ErrorResponse("JWT verification failed")) //ErrorResponse는 "JWT verification failed"라는 메시지를 담고 있습니다.
        response.writer.write(jsonString) //이 문자열을 response.writer를 사용하여 응답에 작성합니다
    }

}
