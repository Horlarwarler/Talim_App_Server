package com.drcescent.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class CategoryModel (
    val categoryId :Int? = null,
    val name:String,
    val _id: String? =  null
)