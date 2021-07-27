package com.sbrati.spring.boot.starter.kotlin.telegram.context

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context

data class CommandExecutionContext(
    var commandName: String,
    var currentStage: String? = null,
    var currentStageStarted: Boolean = false,
    var context: Context
)
