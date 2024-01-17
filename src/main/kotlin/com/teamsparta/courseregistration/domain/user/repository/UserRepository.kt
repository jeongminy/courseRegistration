//package com.teamsparta.courseregistration.domain.user.repository
//
//
//import com.teamsparta.courseregistration.domain.user.model.User
//import org.springframework.data.jpa.repository.JpaRepository
//import org.springframework.data.jpa.repository.Query
//
//interface UserRepository: JpaRepository<User, Long> {
//    fun existsByEmail(email: String): Boolean
//
////  fun findByEmail(email: String): User? // 이 방법은 JPQL 이고, 이렇게도 쓸수있고 아래 Query 로도 쓸수 있어. // 아래와 같은 역할을 함.
//
//    @Query("select u from User u where u.email=:email")
//    fun findByEmail(email: String): User?
//}

package com.teamsparta.courseregistration.domain.user.repository

import com.teamsparta.courseregistration.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {

    fun findByEmail(email: String) : User?

    fun existsByEmail(email: String): Boolean
}