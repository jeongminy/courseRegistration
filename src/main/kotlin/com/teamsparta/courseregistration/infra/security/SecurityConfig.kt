package com.teamsparta.courseregistration.infra.security

import com.teamsparta.courseregistration.infra.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity //http관련 보안 기능을 설정 하기 위해
@EnableMethodSecurity //MethodSecurity를 활성화 한다.(MethodSecurity는 Spring Security가 제공하는 기능으로, 메서드 수준에서의 보안과 인가를 처리하는 기능입니다.)
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter, //토큰 인증을 하지 않는 사람은 filter를 통과 할 수 없도록 하기 위해 주입!
    private val authenticationEntryPoint: AuthenticationEntryPoint, //JWT 인증 실패시 발생할 수 있는 예외 처리를 위해 주입
    private val accessDeniedHandler: AccessDeniedHandler
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        //filterChain 함수는 Spring Security에서 사용되는 SecurityFilterChain 객체를 생성하는 함수입니다.
        //이 함수는 http라는 HttpSecurity 객체를 매개변수로 받아와서 SecurityFilterChain을 구성하고 반환합니다.
        //즉, HTTP 요청에 대한 인가 규칙을 설정하는 부분입니다. 즉, 어떤 요청이 허용되는지, 인증이 필요한지 등을 지정할 수 있습니다.
        return http
            .httpBasic { it.disable() } // BasicAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter 제외
            .formLogin { it.disable() } // UsernamePassworedAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter 제외
            .csrf { it.disable() } // CsrfFilter 제외
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/login", //로그인 아무나 할거야
                    "/signup", //회원가입 아무나 할거야
                    "/swagger-ui/**", //Swagger 문을 열어줘
                    "/v3/api-docs/**" //Swagger 문을 열어줘
                ).permitAll() //해당 경로에 대해서는 모든 사용자에게 접근을 허용합니다. (인증하지 않아도 접근 가능)
                    //.requestMatchers("/admin/**").hasRole("ADMIN")  관리자일 경우만 해당 경로로 접근 할수 있음 설정 (URL기반)
                    .anyRequest().authenticated() //anyRequest() 메서드는 나머지 모든 요청에 대해서 권한이 필요하다는 것을 의미
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            //addFilterBefore 메서드를 사용하여 jwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 이전에 수행하도록 함.
            // jwtAuthenticationFilter가 UsernamePasswordAuthenticationFilter 이전에 실행해야 하는 이유는? 인증 방식의 우선순위, 효율성, 보안 측면에서 유리함.

            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                //authenticationEntryPoint(authenticationEntryPoint)은
                //인증되지 않은 요청이 접근하려고 할 때 호출되는 인증 진입 지점을 설정합니다.
                // 인증되지 않은 사용자가 접근할 경우, 해당 지점에서 인증에 필요한 동작을 수행하게 됩니다.

                it.accessDeniedHandler(accessDeniedHandler)
                //accessDeniedHandler(accessDeniedHandler)는
                //인증된 사용자가 요청에 대한 접근 권한이 없을 때 호출되는 접근 거부 처리자를 설정합니다.
                //인증된 사용자가 요청에 대한 권한이 없는 경우, 해당 처리자를 통해 접근 거부에 대한 동작을 수행하게 됩니다.
            }
            .build()
    }
}