package com.teamsparta.courseregistration.domain.course.dto

import com.teamsparta.courseregistration.domain.lecture.dto.LectureResponse

data class CourseResponse(
    val id: Long, // 단순히 데이터의 전달용이기 때문에 구지 변경 가능할 필요는 없기 때문에 수정불가능하게 val //Long으로 한 이유는 아이디를 더 길게 쓸수있게
    val title: String, // Course의 제목
    val description: String?, // Course에 대한 설명 - optional(해당값이 있을수도있고 없을수도있고) 할 수 있으므로 nullable 설정
    val status: String, // 마감상태
    val maxApplicants: Int, // 신청 가능 인원(30명)
    val numApplicants: Int, // 현재 신청 인원
    val lectures: List<LectureResponse>
)

// 왜 데이터클래스(DTO)로 작성했을까?
// 장점1 : toString(), equals(), hashCode() 등의 메서드를 자동으로 생성
// 장점2 : val 키워드를 사용해서 불변성을 유지하기 좋음!
// 장점3 : 컴파일 하게되면 자동으로 component()메서드가 생성 -> 구조 분해 선언이나 패턴 매칭이 가능해짐