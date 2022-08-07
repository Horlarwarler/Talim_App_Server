package com.drcescent.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class DatabaseCategoryModel(

    val categoryId:Int,
    val name:String,
    val creationDate: String,
    @BsonId
    val _id: Id<DatabaseCategoryModel>? = null,
)
data class DatabaseCategoryCounter(
    val currentCounter :Int
)
