package com.drcescent.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtInterfaceImpl : JwtInterface {

    override fun generateToken(jwtConfig: JwtConfig, vararg jwtClaims: JwtClaim): String {
       var jwtBuilder = JWT
           .create()
           .withAudience(jwtConfig.audience)
           .withIssuer(jwtConfig.issuer)
           .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.expirationDate))
        jwtClaims.forEach { claim ->
            jwtBuilder = jwtBuilder.withClaim(claim.name, claim.value)
        }


        return jwtBuilder.sign(Algorithm.HMAC256(jwtConfig.secretKey))
    }

    override fun verifyToken(jwtConfig: JwtConfig,): JWTVerifier {
        return JWT
            .require(Algorithm.HMAC256(jwtConfig.secretKey))
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .build()
    }
}