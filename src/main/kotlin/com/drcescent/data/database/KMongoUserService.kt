package com.drcescent.data.database

import com.drcescent.data.models.DatabaseCategoryModel
import com.drcescent.data.models.DatabaseLectureModel
import com.drcescent.domain.mapper.convertToCategoryModel
import com.drcescent.domain.mapper.convertToLectureModel
import com.drcescent.domain.models.CategoryModel
import com.drcescent.domain.models.LectureModel
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class KMongoUserService(
    db: CoroutineDatabase
):  UserDbInterface{
     private val lectures = db.getCollection<DatabaseLectureModel>()
    private val categories = db.getCollection<DatabaseCategoryModel>()

    override suspend fun getAllMusic(): List<LectureModel> {
        val lectures = lectures.find().toList().map{
            it.convertToLectureModel()
        }
        return  lectures

    }

    override suspend fun getMusic(lectureName: String): LectureModel? {
        return lectures.findOne(
            DatabaseLectureModel::lectureName eq lectureName
        )?.convertToLectureModel()
    }

    override suspend fun getAllCategory(): List<CategoryModel> {
        return categories.find().toList().map {
            it.convertToCategoryModel()
        }
    }
}