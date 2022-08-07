package com.drcescent.security.hash

interface HashInterface {
    fun generateHash(password:String): SaltedHash
    fun verifyHash(password: String, saltedHash: SaltedHash):Boolean
}