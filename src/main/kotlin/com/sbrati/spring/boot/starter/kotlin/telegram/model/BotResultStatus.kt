package com.sbrati.spring.boot.starter.kotlin.telegram.model

enum class BotResultStatus {

    OK, FORBIDDEN
}

fun Int.getBotResultStatus(): BotResultStatus {
    if (this == 403) {
        return BotResultStatus.FORBIDDEN
    }
    return BotResultStatus.OK
}