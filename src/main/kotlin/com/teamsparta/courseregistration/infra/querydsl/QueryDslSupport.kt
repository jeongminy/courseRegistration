package com.teamsparta.courseregistration.infra.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

//이 클래스를 상속받은 구체적인 클래스에서는
//queryFactory를 사용하여 QueryDSL을 활용하여 SQL 쿼리를 작성하고 실행할 수 있습니다.
abstract class QueryDslSupport {

    @PersistenceContext
    //해당 어노테이션을 사용하여 EntityManager를 주입받습니다.
    //EntityManager는 JPA(Java Persistence API)를 사용하여
    //데이터베이스와의 상호 작용을 담당하는 중요한 컴포넌트입니다.
    protected lateinit var entityManager: EntityManager


    //JPAQueryFactory는 QueryDSL에서 제공하는 쿼리 작성을 위한 핵심 클래스로,
    //EntityManager를 기반으로 쿼리를 생성하고 실행할 수 있습니다.
    protected val queryFactory: JPAQueryFactory
        get() {
            return JPAQueryFactory(entityManager)
        }
}