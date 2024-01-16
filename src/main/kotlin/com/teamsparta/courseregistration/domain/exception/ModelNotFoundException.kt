package com.teamsparta.courseregistration.domain.exception

data class ModelNotFoundException(val modelName: String, val id: Long?) : // RuntimeException은 app이 실행되었을때 발생할수 있는 예외이다.
    RuntimeException("Model $modelName not found with given id: $id")