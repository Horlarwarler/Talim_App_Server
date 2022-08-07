package com.drcescent.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class DatabaseAdminModel
    (

    val adminId: Int,
    val username:String,
    val hashPassword:String,
    val isSuperAdmin:Boolean = false,
    val saltValue:String,
    val creationDate: String,
    @BsonId
    val _id: Id<DatabaseAdminModel>? = null,
)

data class DatabaseAdminCounter(
    val currentCounter:Int
)