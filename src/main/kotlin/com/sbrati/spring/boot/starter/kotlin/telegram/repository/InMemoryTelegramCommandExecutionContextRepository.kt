package com.sbrati.spring.boot.starter.kotlin.telegram.repository

import com.sbrati.spring.boot.starter.kotlin.telegram.context.CommandExecutionContext
import java.util.concurrent.ConcurrentHashMap

class InMemoryTelegramCommandExecutionContextRepository(private val contexts: MutableMap<Long, CommandExecutionContext> = ConcurrentHashMap())
    : TelegramCommandExecutionContextRepository {

    override fun store(commandContext: CommandExecutionContext) {
        contexts[commandContext.context.chatId!!] = commandContext
    }

    override fun retrieveAndLock(chatId: Long): CommandExecutionContext? {
        return contexts[chatId]
    }

    override fun storeAndAcquire(commandContext: CommandExecutionContext) {
        store(commandContext)
    }

    override fun storeAndRelease(commandContext: CommandExecutionContext) {
        store(commandContext)
    }

    override fun delete(chatId: Long) {
        contexts.remove(chatId)
    }
}