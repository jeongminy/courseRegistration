package com.teamsparta.courseregistration.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class JwtPlugin(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour: Long
) {

//    companion object {
//        const val SECRET = "PO4c8z41Hia5gJG3oeuFJMRYBB4Ws4aZ"
//        const val ISSUER = "team.sparta.com"
//        const val ACCESS_TOKEN_EXPIRATION_HOUR: Long = 168
//    }

//    fun test(){
//        validateToken("abc").onSuccess {
//            it.header
//        }.onFailure{
//            it.
//        }
//    }


    //주어진 토큰을 검증하고 파싱(해석)하는 기능을 수행
    fun validateToken(jwt: String): Result<Jws<Claims>>{
        //Result는 Kotlin 표준 라이브러리에서 제공하는 클래스로, 작업 수행 중에 예외가 발생할 수 있는 경우 예외 처리를 보다 편리하게 할 수 있도록 도와줍니다. Result는 작업의 성공 또는 실패를 나타내는 Success와 Failure 두 가지 하위 클래스를 가지고 있습니다.
        return kotlin.runCatching { //runCatching 함수를 사용하여 예외가 발생할 수 있는 부분을 감싸고 있습니다.
            val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }
    }

    //토큰(Access Token)을 생성하는 역할
    fun generateAccessToken(subject: String, email: String, role: String): String {
        return generateToken(subject, email, role, Duration.ofHours(accessTokenExpirationHour))
    }


    //토큰(Access Token)을 실질적으로 생성하는 역할
    private fun generateToken(subject: String, email: String, role: String, expirationPeriod: Duration): String {
        val claims: Claims = Jwts.claims()
            .add(mapOf("role" to role, "email" to email))
            .build()

        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        //Keys.hmacShaKeyFor 메소드는 HMAC 알고리즘을 사용하는 키를 생성하는데, 주로 서명에 활용됨.

        val now = Instant.now()

        return Jwts.builder()
            .subject(subject) //토큰의 주제 (어떤 주체에게 속하는지를 나타냄)
            .issuer(issuer) //토큰의 발급자 (일반적으로 서버의 도메인)
            .issuedAt(Date.from(now)) //토큰의 발행 시간
            .expiration(Date.from(now.plus(expirationPeriod))) //토큰의 만료시간
            .claims(claims) //토큰의 추가정보
            .signWith(key) //토큰을 서명하기 위한 키 설정, 토큰이 유효한지를 검증
            .compact() //모든 설정이 완료된 후에 토큰을 생성하고, 이를 문자열로 변환합니다.

        //이렇게 설정된 값들은 JWT의 헤더(Header), 페이로드(Payload), 서명(Signature)에 해당함.
    }
}