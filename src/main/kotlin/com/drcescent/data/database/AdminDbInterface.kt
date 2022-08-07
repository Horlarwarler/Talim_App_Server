package com.drcescent.data.database

import com.drcescent.data.models.DatabaseAdminModel
import com.drcescent.data.models.DatabaseCategoryModel
import com.drcescent.domain.models.AdminModel
import com.drcescent.domain.models.CategoryModel
import com.drcescent.domain.models.LectureModel

interface AdminDbInterface {
    suspend fun uploadLecture(lectureModel: LectureModel):Boolean
    suspend fun editLecture(lectureModel: LectureModel):Boolean
    suspend fun deleteLecture(uniqueId:String):Boolean

    suspend fun createCategory(categoryModel: CategoryModel):Boolean
    suspend fun deleteCategory(uniqueId:String):Boolean
    suspend fun editCategory(categoryModel: CategoryModel):Boolean


}