package com.drcescent.util

import com.drcescent.domain.models.LectureModel
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.lectureModelFromRequest(): LectureModel? {
    return call.receiveOrNull()
}