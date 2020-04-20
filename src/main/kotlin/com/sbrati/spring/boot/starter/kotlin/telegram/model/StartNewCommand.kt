package com.sbrati.spring.boot.starter.kotlin.telegram.model

class StartNewCommand(val commandName: String)

fun startNewCommand(commandName: String): StartNewCommand {
    return StartNewCommand(commandName)
}