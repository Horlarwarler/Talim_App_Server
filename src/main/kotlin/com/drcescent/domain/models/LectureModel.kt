package com.drcescent.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.io.InputStream
import java.util.*
import java.util.stream.Stream

@Serializable
data class LectureModel(
    val lectureId: Int? = null,
    val categoryId: Int,
    val description: String,
    val lectureName: String,
    val length: Double,
    val lectureUrl:String,
    val id: String?= null
)

