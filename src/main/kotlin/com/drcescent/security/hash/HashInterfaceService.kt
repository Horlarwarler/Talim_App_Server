package com.drcescent.security.hash

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

class HashInterfaceService: HashInterface {
    override fun generateHash(password: String): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32)
        val saltString = Hex.encodeHexString(salt)
        val hashedPassword = DigestUtils.sha256Hex(saltString + password)
        return SaltedHash(
            salt = saltString,
            hashedPassword = hashedPassword
        )
    }

    override fun verifyHash(password: String, saltedHash: SaltedHash): Boolean {
        return saltedHash.hashedPassword == DigestUtils.sha256Hex(saltedHash.salt + password)
    }
}
