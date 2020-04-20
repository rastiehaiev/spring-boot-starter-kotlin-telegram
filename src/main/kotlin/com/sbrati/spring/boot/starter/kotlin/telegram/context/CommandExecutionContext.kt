package com.sbrati.spring.boot.starter.kotlin.telegram.context

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommand

class CommandExecutionContext(var currentStage: String? = null,
                              var currentStageStarted: Boolean = false,
                              var context: Context,
                              var command: TelegramCommand<out Context>)