package com.drcescent.routing

import com.drcescent.data.database.UserDbInterface
import com.drcescent.domain.models.ServerResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.userRouting(
    userDbInterface:UserDbInterface
){
    get("/all-category"){
        val categories = try {
            userDbInterface.getAllCategory()
        }
        catch (e:Exception){
            null
        }
        categories?.let {

            call.respond(HttpStatusCode.OK, ServerResponse(data = it ))
            return@get
        }
        call.respond(HttpStatusCode.BadRequest,)

    }
    get("/all-lecture") {
        val lectures = try {
            userDbInterface.getAllMusic()
        }
        catch (e:Exception){
            null
        }
        lectures?.let {
            call.respond(HttpStatusCode.OK, ServerResponse( data = it))
            return@get

        }
        call.respond(HttpStatusCode.BadRequest,)
    }
}

fun Routing.getMusic(
    userDbInterface: UserDbInterface
){
    get("lecture/{lectureName}"){
        val lectureName = call.parameters["lectureName"]!!
        val lectureUrl = userDbInterface.getMusic(lectureName)
        lectureUrl?.let {
            call.respond(HttpStatusCode.OK,)
            return@get
        }
        call.respond(HttpStatusCode.NotFound,)
    }
}