package com.sbrati.spring.boot.starter.kotlin.telegram.repository

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommand
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TelegramCommandRepository(private val commands: List<TelegramCommand<out Context>>?) {

    private var commandMap: Map<String, TelegramCommand<out Context>> = HashMap()

    fun findByName(name: String): TelegramCommand<out Context>? {
        return commandMap[name]
    }

    @PostConstruct
    private fun initialize() {
        if (commands != null) {
            commandMap = commands.associateBy { it.name }
        }
    }
}