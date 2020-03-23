package com.sbrati.spring.boot.starter.kotlin.telegram.context

import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommand
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandProgress
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class TelegramCommandContextRepository {

    private val contexts: MutableMap<Long, CommandContext> = ConcurrentHashMap()

    fun findByChatId(chatId: Long): CommandContext? {
        return contexts[chatId]
    }

    fun removeByChatId(chatId: Long) {
        contexts.remove(chatId)
    }

    fun create(chatId: Long, command: TelegramCommand<out TelegramCommandProgress>): CommandContext {
        val commandContext = CommandContext(command.name)
        contexts[chatId] = commandContext
        return commandContext
    }
}