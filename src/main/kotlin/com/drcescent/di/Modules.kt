package com.drcescent.di

import com.drcescent.data.database.*
import com.drcescent.security.hash.HashInterface
import com.drcescent.security.hash.HashInterfaceService
import com.drcescent.security.jwt.JwtInterface
import com.drcescent.security.jwt.JwtInterfaceImpl
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        KMongo
            .createClient(
            "mongodb://localhost:27017",
            )
            .coroutine
            .getDatabase("talimLecture")
    }
    single<JwtInterface> {
        JwtInterfaceImpl()
    }
    single<HashInterface>{
        HashInterfaceService()
    }
    single<SuperAdminInterface> {
        SuperAdminService(get(),get())
    }
    single<UserDbInterface> {
        KMongoUserService(get())
    }
    single<AdminDbInterface> {
        KMongoAdminService(get())
    }
}

