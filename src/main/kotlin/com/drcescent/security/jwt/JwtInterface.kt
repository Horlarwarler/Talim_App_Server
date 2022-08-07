package com.drcescent.security.jwt

import com.auth0.jwt.JWTVerifier

interface JwtInterface {
    fun generateToken(
        jwtConfig: JwtConfig,
        vararg  jwtClaims: JwtClaim
    ):String

    fun verifyToken(
        jwtConfig: JwtConfig,
    ): JWTVerifier
}