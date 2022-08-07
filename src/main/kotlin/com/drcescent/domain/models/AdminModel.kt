package com.drcescent.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AdminModel(
    val adminId:Int? = null,
    val username:String,
    val password:String,
    val isSuperAdmin:Boolean = false,
    val _id : String? = null,
)
