package com.sbrati.spring.boot.starter.kotlin.telegram.model.stages

class JumpToStage(val stage: String)

fun jumpTo(stage: String): JumpToStage {
    return JumpToStage(stage)
}