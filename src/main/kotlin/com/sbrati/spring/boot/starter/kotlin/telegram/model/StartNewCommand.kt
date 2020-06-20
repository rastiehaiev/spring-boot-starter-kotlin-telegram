package com.sbrati.spring.boot.starter.kotlin.telegram.model

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context

class StartNewCommand(val commandName: String, val context: Context?)

fun startNewCommand(commandName: String, context: Context? = null): StartNewCommand {
    return StartNewCommand(commandName, context)
}