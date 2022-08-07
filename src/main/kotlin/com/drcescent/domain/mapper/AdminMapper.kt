package com.drcescent.domain.mapper

import com.drcescent.data.models.DatabaseAdminModel
import com.drcescent.domain.models.AdminModel
import com.drcescent.security.hash.SaltedHash
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun DatabaseAdminModel.convertToAdminModel():AdminModel{
    return AdminModel(
        adminId,
        username,
        hashPassword,
        isSuperAdmin,
        _id!!.toString()
    )
}

fun AdminModel.convertToDatabaseAdminModel(adminId:Int?=null, hash: SaltedHash):DatabaseAdminModel{
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formatted = current.format(formatter)!!
    val id = this.adminId ?: adminId!!

    return DatabaseAdminModel(
        adminId = id,
        creationDate = formatted,
        username = this.username,
        hashPassword = hash.hashedPassword,
        isSuperAdmin = this.isSuperAdmin,
        saltValue = hash.salt
    )
}