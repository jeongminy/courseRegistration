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
        //Result는 Kotlin 표준 라이브러리에서 제공하는 클래스로, 작업 수행 중에 예외가 발생할 수 있는 경우 예외 처리를 보다 편리하게 할 수 있도록 도와줍니다.
        // Result는 작업의 성공 또는 실패를 나타내는 Success와 Failure 두 가지 하위 클래스를 가지고 있습니다.

        return kotlin.runCatching { //runCatching 함수를 사용하여 예외가 발생할 수 있는 부분을 감싸고 있습니다. //try-catch 구문을 좀더 우아하게 표현함! (예외를 처리)
            val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
            //이 코드는 secret라는 문자열로 표현된 비밀 키를 HMAC-SHA 알고리즘에 사용할 수 있는 형태의 키로 변환하는 역할을 합니다.
            //여기서 secret는 문자열로 표현된 비밀 키입니다. toByteArray 메서드를 사용하여 secret 문자열을 바이트 배열로 변환합니다.
            //이때 StandardCharsets.UTF_8을 인코딩으로 사용하여 문자열을 바이트 배열로 변환합니다. 인코딩은 문자열을 바이트로 변환하는 과정에서 문자 인코딩 방식을 지정하는 것입니다. UTF-8은 유니코드 문자 인코딩 방식 중 하나로, 대부분의 문자를 효율적으로 표현할 수 있는 방식입니다.
            //secret.toByteArray(StandardCharsets.UTF_8)를 통해 얻어진 바이트 배열은 HMAC-SHA 알고리즘에서 사용할 수 있는 형태의 키입니다. Keys.hmacShaKeyFor 메서드는 이 바이트 배열을 HMAC 키로 변환하여 key 변수에 할당합니다.
            //즉, Keys.hmacShaKeyFor 메서드는 주어진 문자열로 표현된 비밀 키를 UTF-8 인코딩을 사용하여 바이트 배열로 변환한 뒤, 그 바이트 배열을 HMAC 키로 변환하여 반환합니다. 이렇게 생성된 key는 HMAC-SHA 알고리즘에서 JWT의 서명 검증에 사용됩니다.

            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
            //Jwts.parser(): JWT를 파싱하기 위한 JWT 파서를 생성합니다.
            //.verifyWith(key): 생성된 JWT 파서에 서명 검증에 사용할 키를 설정합니다. 앞서 생성한 key 변수가 여기에 사용됩니다. 이렇게 설정된 키를 사용하여 JWT의 서명을 검증합니다.
            //.build(): JWT 파서를 빌드하여 JWT 파싱 및 검증을 수행할 준비를 합니다.
            //.parseSignedClaims(jwt): 준비된 JWT 파서를 사용하여 주어진 JWT를 파싱하고 서명을 검증합니다. jwt는 검증할 JWT를 의미합니다. 이 과정에서 JWT의 헤더와 페이로드를 추출하고, 서명을 검증하여 토큰의 무결성을 확인합니다. 검증이 성공하면 JWT의 클레임(claim)을 반환합니다. 클레임은 토큰에 포함된 정보를 나타내며, 검증된 토큰의 내용을 확인할 수 있습니다.
            //즉, 주어진 코드는 key를 사용하여 JWT를 파싱하고 서명을 검증하는 과정을 수행합니다. 이를 통해 JWT의 무결성을 확인하고, 클레임을 추출하여 토큰에 포함된 정보를 확인할 수 있습니다.
        }
    }

    //토큰(Access Token)을 생성하는 역할
    fun generateAccessToken(subject: String, email: String, role: String): String {
        return generateToken(subject, email, role, Duration.ofHours(accessTokenExpirationHour))
    }


    //토큰(Access Token)을 실질적으로 생성하는 역할
    private fun generateToken(subject: String, email: String, role: String, expirationPeriod: Duration): String {
        val claims: Claims = Jwts.claims() //JWT의 클레임(claim)을 생성하기 위한 메서드입니다. 이 메서드를 호출하면 클레임을 생성할 준비가 됩니다.
            .add(mapOf("role" to role, "email" to email)) //생성된 클레임에 키-값 쌍 형태의 정보를 추가합니다. 여기서는 "role"과 "email"이라는 키에 해당하는 값을 추가하고 있습니다. role과 email은 메소드의 인자로 전달된 값입니다.
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