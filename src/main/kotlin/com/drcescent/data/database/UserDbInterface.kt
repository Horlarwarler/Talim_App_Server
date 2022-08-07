package com.drcescent.data.database

import com.drcescent.data.models.DatabaseCategoryModel
import com.drcescent.data.models.DatabaseLectureModel
import com.drcescent.domain.models.CategoryModel
import com.drcescent.domain.models.LectureModel

interface UserDbInterface {
    suspend fun getAllMusic(): List<LectureModel>
    suspend fun getMusic(lectureName:String):LectureModel?
    suspend fun getAllCategory():List<CategoryModel>
}