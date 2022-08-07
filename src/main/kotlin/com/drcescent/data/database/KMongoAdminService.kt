package com.drcescent.data.database

import com.drcescent.data.models.*
import com.drcescent.domain.mapper.*
import com.drcescent.domain.models.CategoryModel
import com.drcescent.domain.models.LectureModel
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class KMongoAdminService(
    db:CoroutineDatabase,
): AdminDbInterface {

    private val lectures = db.getCollection<DatabaseLectureModel>()
    private val categories = db.getCollection<DatabaseCategoryModel>()
    private val lectureCounter = db.getCollection<LectureCounter>()
    private val adminCounter = db.getCollection<DatabaseAdminCounter>()
    private val categoryCounter = db.getCollection<DatabaseCategoryCounter>()

    private suspend fun retrieveAndUpdateLectureCounter(isDelete: Boolean = false):Int{
        val lectures = lectureCounter.find().toList()
        return if (lectures.isEmpty()){
            lectureCounter.insertOne(
                LectureCounter(0)
            )
            0
        } else{
            val currentLectureId = lectureCounter.find().first()?.currentId!!
            val incrementValue = if(isDelete)-1 else 1
            lectureCounter.findOneAndUpdate(
                filter = LectureCounter::currentId eq currentLectureId,
                update = inc(LectureCounter::currentId, incrementValue)
            )
            incrementValue + currentLectureId
        }


    }


    private suspend fun retrieveAndUpdateCategoryCounter(isDelete:Boolean = false):Int{
        val categories = categoryCounter.find().toList()
        return if (categories.isEmpty()){
            categoryCounter.insertOne(
                DatabaseCategoryCounter(0)
            )
            0
        } else{
            val currentCategoryId = categoryCounter.find().first()?.currentCounter!!
            val incrementValue = if(isDelete)-1 else 1
            categoryCounter.findOneAndUpdate(
                filter = DatabaseCategoryCounter::currentCounter eq currentCategoryId,
                update = inc(DatabaseCategoryCounter::currentCounter, incrementValue)
            )
            currentCategoryId + incrementValue
        }


    }
    override suspend fun uploadLecture(lectureModel: LectureModel): Boolean {
        val lectureId = retrieveAndUpdateLectureCounter()
        return lectures.insertOne(lectureModel.convertToDatabaseLectureModel(lectureId)).wasAcknowledged()
    }

    override suspend fun editLecture(lectureModel: LectureModel): Boolean {
        return  lectures.updateOne(
            DatabaseLectureModel::lectureName eq lectureModel.lectureName,
            lectureModel.convertToDatabaseLectureModel()).wasAcknowledged()
    }

    override suspend fun deleteLecture(uniqueId: String): Boolean {
        val counter = retrieveAndUpdateLectureCounter(true)
        val objectId = ObjectId(uniqueId)
        val lectureToRemove = lectures.findOneById(objectId)
        println("Here is the catergory $lectureToRemove and $objectId")

        if(counter<= 0 && lectureToRemove != null){
            lectures.drop()
            lectureCounter.drop()
        }
        else{
            lectures.deleteOneById (
                objectId
            )
            // Updates the lectures Ids
            lectureToRemove?.let {
                    lecture->
                val removedLectureId = lecture.lectureId
                return lectures.updateMany(
                    DatabaseLectureModel::lectureId gt removedLectureId,
                    inc(DatabaseLectureModel::lectureId,-1)
                ).wasAcknowledged()

            }
        }




        return false
    }



    private suspend fun categoryExist(username: String):DatabaseCategoryModel?{
        return categories.findOne(
            DatabaseCategoryModel::name eq username
        )
    }
    override suspend fun createCategory(categoryModel: CategoryModel): Boolean {
        return if (categoryExist(categoryModel.name) == null){
            val categoriesCounter = retrieveAndUpdateCategoryCounter()
            categories.insertOne(categoryModel.convertToDatabaseCategoryModel(categoriesCounter)).wasAcknowledged()
        } else{
            false
        }
    }

    override suspend fun deleteCategory(uniqueId: String): Boolean {
        val categoriesCounter = retrieveAndUpdateCategoryCounter(true)
        val categoryObject = ObjectId(uniqueId)
        val categoryToDelete = categories.findOneById(categoryObject)
        if ( categoriesCounter <= 0 && categoryToDelete !=null){
            categories.drop()
            categoryCounter.drop()
        }
        else{
                categories.deleteOneById(categoryObject)
                categoryToDelete?.let {
                    return categories.updateMany(
                        DatabaseCategoryModel::categoryId gt it.categoryId,
                        inc(DatabaseCategoryModel::categoryId, -1)
                    ).wasAcknowledged()
                }


        }
        return false
    }
    override suspend fun editCategory(categoryModel: CategoryModel): Boolean {

        return categories.updateOne(
            DatabaseCategoryModel::categoryId eq categoryModel.categoryId!!,
            categoryModel.convertToDatabaseCategoryModel()).wasAcknowledged()
    }


}

