package com.sbrati.spring.boot.starter.kotlin.telegram.model


data class BotCommands(val commands: Map<String, List<BotCommandSpec>>)

data class BotCommandSpec(val command: String, val descriptionKey: String)