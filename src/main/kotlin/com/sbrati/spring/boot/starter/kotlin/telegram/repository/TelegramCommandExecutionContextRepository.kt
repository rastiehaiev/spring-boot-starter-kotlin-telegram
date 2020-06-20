package com.sbrati.spring.boot.starter.kotlin.telegram.repository

import com.sbrati.spring.boot.starter.kotlin.telegram.context.CommandExecutionContext


interface TelegramCommandExecutionContextRepository {

    fun store(commandContext: CommandExecutionContext)

    fun retrieveAndLock(chatId: Long): CommandExecutionContext?

    fun storeAndAcquire(commandContext: CommandExecutionContext)

    fun storeAndRelease(commandContext: CommandExecutionContext)

    fun delete(chatId: Long)
}