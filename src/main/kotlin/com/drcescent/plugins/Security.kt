package com.drcescent.plugins

import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.drcescent.security.jwt.JwtConfig
import com.drcescent.security.jwt.JwtInterface
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val jwtInterface by inject<JwtInterface>()
    val audience = environment.config.property("jwt.audience").getString()
    val domain = environment.config.property("jwt.domain").getString()
    val secretKey = System.getenv("secretKey")
    val jwtConfig = JwtConfig(
        audience = audience,
        issuer = domain,
        secretKey = secretKey,
        expirationDate = 30L * 24L * 60L * 60L
    )
    authentication {
        jwt {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier {jwtInterface.verifyToken(jwtConfig)  }
            validate { credential ->
                if (credential.payload.getClaim("userName").toString().isNotEmpty()) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
