package com.sbrati.spring.boot.starter.kotlin.telegram.context

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context

class CommandExecutionContext(var commandName: String,
                              var currentStage: String? = null,
                              var currentStageStarted: Boolean = false,
                              var context: Context) {

    override fun toString(): String {
        return "CommandExecutionContext(commandName='$commandName', currentStage=$currentStage, currentStageStarted=$currentStageStarted, context=$context)"
    }
}

