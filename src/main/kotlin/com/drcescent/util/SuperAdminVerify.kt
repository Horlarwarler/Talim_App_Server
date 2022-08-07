package com.drcescent.util

import com.drcescent.data.database.AdminDbInterface
import com.drcescent.data.database.SuperAdminInterface
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.verifySuperAdmin(
    superAdminInterface: SuperAdminInterface
): Boolean{
    val loggedInUser = call.principal<JWTPrincipal>()!!.payload.getClaim("userName")!!.asString()

    return superAdminInterface.userExist(loggedInUser)!!.isSuperAdmin
}