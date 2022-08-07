package com.drcescent.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import java.util.*


data class DatabaseLectureModel(

    val lectureId: Int,
    val date: String,
    val categoryId: Int,
    val description: String,
    val lectureName: String,
    val length: Double,
    val lectureUrl:String,
    @BsonId
    val _id: Id<DatabaseLectureModel>? = null,
)


data class  LectureCounter(
    val currentId:Int = 0
)

