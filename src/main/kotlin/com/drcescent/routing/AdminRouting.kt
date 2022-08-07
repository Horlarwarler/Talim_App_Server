package com.drcescent.routing

import com.drcescent.data.database.AdminDbInterface
import com.drcescent.data.database.SuperAdminInterface
import com.drcescent.domain.models.AdminModel
import com.drcescent.domain.models.CategoryModel
import com.drcescent.domain.models.ServerResponse
import com.drcescent.security.hash.HashInterface
import com.drcescent.security.hash.SaltedHash
import com.drcescent.security.jwt.JwtClaim
import com.drcescent.security.jwt.JwtConfig
import com.drcescent.security.jwt.JwtInterface
import com.drcescent.util.lectureModelFromRequest
import com.drcescent.util.verifySuperAdmin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.login(
    superAdminInterface: SuperAdminInterface,
    hashInterface: HashInterface,
    jwtInterface: JwtInterface
){

    val audience = environment?.config?.property("jwt.audience")?.getString()!!
    val domain = environment?.config?.property("jwt.domain")?.getString()!!
    val secretKey = System.getenv("secretKey")!!
    val jwtConfig = JwtConfig(
        audience = audience,
        issuer = domain,
        secretKey = secretKey,
        expirationDate = 30L * 24L * 60L * 60L * 1000L
    )
    get("/login"){
        val request = call.receiveOrNull<AdminModel>()?:run {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }

        val dbUser = superAdminInterface.userExist(request.username)
        if(dbUser == null){
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        //val jwtConfig  = JwtConfig()
        val saltedHash = SaltedHash(salt = dbUser.saltValue, hashedPassword = dbUser.hashPassword )
        val userValid =  hashInterface.verifyHash(
            password = request.password,
            saltedHash = saltedHash
        )

        if(userValid ){
            val tokenClaims = arrayOf(
                JwtClaim("userName",dbUser.username),
               // JwtClaim("isSuperAdmin", if(dbUser.isSuperAdmin)"Yes" else "No")
            )
            val token = jwtInterface.generateToken(
                jwtConfig = jwtConfig,
                jwtClaims = tokenClaims
            )
            call.respond(HttpStatusCode.OK, ServerResponse(isSuccessful = true, data = token))
            }
        else {
            call.respond(HttpStatusCode.NotAcceptable, ServerResponse(isSuccessful = true, data = "Incorrect Email Or Password"))
        }
        }



        // Verify Credentials
}

fun Routing.superAdminActions(
    superAdminInterface: SuperAdminInterface
) {
    authenticate {

        post("/create-user") {
//          //  val isSuperAdmin = verifySuperAdmin(superAdminInterface)
//            val loggedInUser = call.principal<JWTPrincipal>()!!.payload.getClaim("userName")!!.asString()
//            print("The logged user is $loggedInUser\n")
//
//
//            val isSuperAdmin = superAdminInterface.userExist(loggedInUser)!!.isSuperAdmin
            val isSuperAdmin = verifySuperAdmin(superAdminInterface)

            if (!isSuperAdmin) {
                call.respond(HttpStatusCode.Unauthorized,)
                return@post
            }
            val request = call.receiveOrNull<AdminModel>() ?: kotlin.run {
                call.respond(
                    HttpStatusCode.BadRequest)
                return@post
            }
//        val isSuperAdmin = request.isSuperAdmin
//        if(!isSuperAdmin){
//            call.respond(HttpStatusCode.BadRequest, ServerResponse(data = "Not authorized"))
//            return@post
//        }

            val userExist = superAdminInterface.userExist(request.username) != null
            if (userExist) {
                call.respond(
                    HttpStatusCode.Conflict)
                return@post
            }
            val isSuccessful = superAdminInterface.createUser(request)
            if (isSuccessful){
                call.respond(HttpStatusCode.OK)
            }
            else{
                call.respond(HttpStatusCode.BadRequest, )

            }
        }

        get("/delete-user/{username}") {
            val isSuperAdmin = verifySuperAdmin(superAdminInterface)
            if (!isSuperAdmin) {
                call.respond(HttpStatusCode.Unauthorized,)
                return@get
            }
            val username = call.parameters["username"]!!
            val userDelete = superAdminInterface.deleteUser(username)
            if (!userDelete) {
                call.respond(HttpStatusCode.BadRequest,)
                return@get
            }
            call.respond(HttpStatusCode.OK,)

        }
        get("/users") {
            val isSuperAdmin = verifySuperAdmin(superAdminInterface)
            if (!isSuperAdmin) {
                call.respond(HttpStatusCode.Unauthorized,)
                return@get
            }
            val admins = superAdminInterface.getAllUsers()
            call.respond(HttpStatusCode.OK, ServerResponse(true, admins))
        }

        get("delete-all-lecture"){
            val isSuperAdmin = verifySuperAdmin(superAdminInterface)
            if (!isSuperAdmin) {
                call.respond(HttpStatusCode.Unauthorized,)
                return@get
            }
            try {
                superAdminInterface.deleteAllLecture()
                call.respond(HttpStatusCode.OK)
            }
            catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("delete-all-category"){
            val isSuperAdmin = verifySuperAdmin(superAdminInterface)
            if (!isSuperAdmin) {
                call.respond(HttpStatusCode.Unauthorized,)
                return@get
            }
            try {
                superAdminInterface.deleteAllCategory()
                call.respond(HttpStatusCode.OK)
            }
            catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("delete-admins"){
            val isSuperAdmin = verifySuperAdmin(superAdminInterface)
            if (!isSuperAdmin) {
                call.respond(HttpStatusCode.Unauthorized,)
                return@get
            }
            try {
                superAdminInterface.deleteAllAdmins()
                call.respond(HttpStatusCode.OK)
            }
            catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest)
            }
        }


    }
}


fun Routing.adminActions(
    adminDbInterface: AdminDbInterface
){
        authenticate {

            post("/create-category"){
                    val category = call.receiveOrNull<CategoryModel>()?: kotlin.run {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    if(
                        category.name.isEmpty() || category.name.length < 5
                    ){
                        call.respond(HttpStatusCode.BadRequest,)
                        return@post
                    }
                    if(!adminDbInterface.createCategory(category)){
                        call.respond(HttpStatusCode.Conflict,)
                        return@post
                    }
                    else{
                        call.respond(HttpStatusCode.OK,)
                    }
            }
            get("/delete-category/{categoryId}") {
                val categoryId = call.parameters["categoryId"]!!

                if(adminDbInterface.deleteCategory(categoryId)){
                    call.respond(HttpStatusCode.OK,ServerResponse(data = "Deleted Successfully") )

                }
                else{
                    call.respond(HttpStatusCode.BadRequest,ServerResponse(data = "Category parameter not valid"))

                }


            }
            put("/edit-category"){
                val category = call.receive<CategoryModel>()
                if(
                    category.name.isEmpty() || category.name.length < 5
                ){
                    call.respond(HttpStatusCode.BadRequest,ServerResponse(data = "Category parameter not valid"))
                    return@put
                }
                if(!adminDbInterface.editCategory(category)){
                    call.respond(HttpStatusCode.Conflict, ServerResponse(data = "Error occurs"))
                    return@put
                }
                else{
                    call.respond(HttpStatusCode.OK, ServerResponse(data = "Successfully Edited"))
                }
            }
            post("/upload-lecture"){

                val lectureModel = lectureModelFromRequest()?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                if(!adminDbInterface.uploadLecture(lectureModel)){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                call.respond(HttpStatusCode.OK)


            }
            put("/edit-lecture"){
                val lectureModel =  lectureModelFromRequest()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
                adminDbInterface.editLecture(lectureModel)
//                if (editMusic == "true"){
//                    val multipart = call.receiveMultipart()
//                    multipart.forEachPart { part ->
//                        if (part is PartData.FileItem){
//                            val fileBytes = part.streamProvider().readBytes()
//                            File("uploads/${lectureModel.lectureName}.mp3").writeBytes(fileBytes)
//                        }
//
//                    }
//                }

            }
            get("/delete-lecture/{lectureId}"){
                val lectureId = call.parameters["lectureId"]!!
                println("this is the $lectureId")
                if(adminDbInterface.deleteLecture(lectureId)){
                    call.respond(HttpStatusCode.OK)
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }


            }
        }
}


//fun Routing.