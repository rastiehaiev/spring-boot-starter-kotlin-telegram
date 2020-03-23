package com.sbrati.spring.boot.starter.kotlin.telegram.command

import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TelegramCommandRepository(private val commands: List<TelegramCommand<out TelegramCommandProgress>>?) {

    private var commandMap: Map<String, TelegramCommand<out TelegramCommandProgress>> = HashMap()

    fun findByName(name: String): TelegramCommand<out TelegramCommandProgress>? {
        return commandMap[name]
    }

    @PostConstruct
    private fun initialize() {
        if (commands != null) {
            commandMap = commands.associateBy { it.name }
        }
    }
}