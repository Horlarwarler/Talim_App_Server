package com.drcescent.plugins

import com.drcescent.data.database.AdminDbInterface
import com.drcescent.data.database.SuperAdminInterface
import com.drcescent.data.database.UserDbInterface
import com.drcescent.routing.*
import com.drcescent.security.hash.HashInterface
import com.drcescent.security.jwt.JwtInterface
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val superAdminInterface by inject<SuperAdminInterface>()
    val jwtInterface by inject<JwtInterface> ()
    val hashInterface by inject<HashInterface>()
    val adminDbInterface by inject<AdminDbInterface>()
    val userDbInterface by inject<UserDbInterface>()
    routing {
        login(superAdminInterface,hashInterface, jwtInterface )
        superAdminActions(superAdminInterface)
        adminActions(adminDbInterface)
        userRouting(userDbInterface)
        getMusic(userDbInterface)

    }
}
