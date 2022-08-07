package com.drcescent.security.jwt


data class JwtConfig(
    val secretKey: String,
    val audience:String,
    val issuer:String,
    val expirationDate:Long
)
