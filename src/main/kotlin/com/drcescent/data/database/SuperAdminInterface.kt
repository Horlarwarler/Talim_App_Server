package com.drcescent.data.database

import com.drcescent.data.models.DatabaseAdminModel
import com.drcescent.domain.models.AdminModel

interface SuperAdminInterface {
    suspend fun createUser(admin: AdminModel):Boolean
    suspend fun deleteUser(username:String):Boolean
    suspend fun userExist(username: String): DatabaseAdminModel?
    suspend fun getAllUsers():List<AdminModel>
    suspend fun deleteAllAdmins()
    suspend fun  deleteAllLecture()
    suspend fun deleteAllCategory()


}