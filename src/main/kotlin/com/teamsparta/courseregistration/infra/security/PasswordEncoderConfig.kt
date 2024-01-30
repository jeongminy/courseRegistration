package com.teamsparta.courseregistration.infra.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class PasswordEncoderConfig {
    //PasswordEncoderConfig 클래스는 Spring의 설정 클래스로서,
    //BCryptPasswordEncoder를 사용하여 비밀번호를 암호화하기 위한 PasswordEncoder 빈을 생성하고 등록하는 역할을 합니다.
    //이를 통해 애플리케이션에서 안전하게 비밀번호를 다룰 수 있습니다.

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    //BCryptPasswordEncoder()를 사용하여 PasswordEncoder를 생성하고 반환
    //BCryptPasswordEncoder는 Spring Security에서 제공하는 비밀번호 암호화 방식 중 하나로, 강력한 암호화를 제공
    //PasswordEncoder는 사용자의 비밀번호를 안전하게 저장하기 위해 사용되며, 입력된 비밀번호를 암호화하거나,
    //저장된 암호화된 비밀번호와 일치하는지 검증하는 등의 기능을 수행
    }
}