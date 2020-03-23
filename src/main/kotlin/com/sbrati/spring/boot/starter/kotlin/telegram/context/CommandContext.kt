package com.sbrati.spring.boot.starter.kotlin.telegram.context

import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandProgress

class CommandContext(val commandName: String) {

    var currentStage: String? = null
    var currentStageStarted: Boolean = false
    var progress: TelegramCommandProgress? = null
}