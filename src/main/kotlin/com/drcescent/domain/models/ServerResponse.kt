package com.drcescent.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse <T>(
    val isSuccessful: Boolean = false,
    val data:T? = null
)