package com.drcescent.security.hash

data class SaltedHash(
    val salt: String,
    val hashedPassword:String
)
