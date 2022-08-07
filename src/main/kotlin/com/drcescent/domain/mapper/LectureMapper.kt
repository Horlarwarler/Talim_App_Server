package com.drcescent.domain.mapper

import com.drcescent.data.models.DatabaseLectureModel
import com.drcescent.domain.models.LectureModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun DatabaseLectureModel.convertToLectureModel():LectureModel{
    return LectureModel(
        lectureId,
        categoryId,
        description,
        lectureName,
        length,
        lectureUrl,
        _id!!.toString()
    )
}

fun LectureModel.convertToDatabaseLectureModel(lectureId: Int? = null):DatabaseLectureModel{
    val id = this.lectureId ?: lectureId!!
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formatted = current.format(formatter)!!
    return DatabaseLectureModel(
        id,
        formatted,
        categoryId,
        description,
        lectureName,
        length,
        lectureUrl
    )
}