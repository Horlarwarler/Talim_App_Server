package com.drcescent.domain.mapper

import com.drcescent.data.models.DatabaseCategoryModel
import com.drcescent.domain.models.CategoryModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun DatabaseCategoryModel.convertToCategoryModel():CategoryModel{
    return CategoryModel(
        categoryId = this.categoryId,
        name = this.name,
        _id = _id!!.toString()

    )

}
fun CategoryModel.convertToDatabaseCategoryModel(id:Int? = null): DatabaseCategoryModel{
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val formatted = current.format(formatter)!!
    val categoryId = this.categoryId ?: id!!
    return DatabaseCategoryModel(
        categoryId =  categoryId,
        name = this.name,
        creationDate = formatted
    )
}