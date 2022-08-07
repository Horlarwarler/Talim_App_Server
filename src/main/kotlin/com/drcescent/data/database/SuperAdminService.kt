package com.drcescent.data.database

import com.drcescent.data.models.*
import com.drcescent.domain.mapper.convertToAdminModel
import com.drcescent.domain.mapper.convertToDatabaseAdminModel
import com.drcescent.domain.models.AdminModel
import com.drcescent.security.hash.HashInterface
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class SuperAdminService(
    db: CoroutineDatabase,
    private val hashInterface: HashInterface
): SuperAdminInterface {
    private val admins = db.getCollection<DatabaseAdminModel>()
    private val adminCounter = db.getCollection<DatabaseAdminCounter>()
    private val lectures = db.getCollection<DatabaseLectureModel>()
    private val categories = db.getCollection<DatabaseCategoryModel>()
    private val lectureCounter = db.getCollection<LectureCounter>()
    private val categoryCounter = db.getCollection<DatabaseCategoryCounter>()

    private suspend fun retrieveAndUpdateAdminCounter(isDelete:Boolean= false):Int{
        val admins = adminCounter.find().toList()
        return if (admins.isEmpty()){
            adminCounter.insertOne(
                DatabaseAdminCounter(0)
            )
            0
        } else{
            val currentAdminId = adminCounter.find().first()?.currentCounter!!
            val incrementValue = if(isDelete)-1 else +1
            adminCounter.findOneAndUpdate(
                filter = DatabaseAdminCounter::currentCounter eq currentAdminId,
                update = inc( DatabaseAdminCounter::currentCounter, incrementValue)
            )

            incrementValue + currentAdminId
        }


    }

    override suspend fun deleteUser(username: String): Boolean {
        val userCounter = retrieveAndUpdateAdminCounter(isDelete = true)
        if(userCounter < 0 && admins.findOne(DatabaseAdminModel::username eq username) != null){
            admins.drop()
        }
        else {
            val userToRemove = admins.findOneAndDelete(
                DatabaseAdminModel::username eq username
            )
            userToRemove?.let { user ->
                val userId = user.adminId
                return admins.updateMany(
                    DatabaseAdminModel::adminId gt userId,
                    inc(DatabaseAdminModel::adminId, -1)
                ).wasAcknowledged()
            }
        }

        return false
    }
    override suspend fun deleteAllLecture() {
        lectures.drop()
        lectureCounter.drop()
    }

    override suspend fun deleteAllCategory() {
        categories.drop()
        categoryCounter.drop()
    }



    override suspend fun getAllUsers(): List<AdminModel> {
        return admins.find().toList().map {
            it.convertToAdminModel()
        }
    }

    override suspend fun deleteAllAdmins() {
        val currentAdminId = adminCounter.find().toList()[0].currentCounter
        adminCounter.findOneAndUpdate(
            DatabaseAdminCounter::currentCounter eq currentAdminId,
            setValue(DatabaseAdminCounter::currentCounter, 0)
        )
        admins.deleteMany(
            DatabaseAdminModel::isSuperAdmin eq false
        )

    }

    override suspend fun userExist(username: String):DatabaseAdminModel?{
        return admins.findOne(
            DatabaseAdminModel::username eq username
        )
    }

    override suspend fun createUser(admin: AdminModel): Boolean {
        val userId = retrieveAndUpdateAdminCounter()
        val userAlreadyExist = userExist(admin.username) != null
        val hashedValue = hashInterface.generateHash(admin.password)
        return if (!userAlreadyExist){
            admins.insertOne(admin.convertToDatabaseAdminModel(userId,hashedValue)).wasAcknowledged()
        } else{
            false
        }
    }


}