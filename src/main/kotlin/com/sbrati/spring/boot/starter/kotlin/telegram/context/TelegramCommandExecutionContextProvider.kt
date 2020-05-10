package com.sbrati.spring.boot.starter.kotlin.telegram.context

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommand
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.component.AdminChatIdsProvider
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import me.ivmg.telegram.entities.Update
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class TelegramCommandExecutionContextProvider(private val commandRepository: TelegramCommandRepository,
                                              private var adminChatIdsProvider: AdminChatIdsProvider?) {

    private val logger by LoggerDelegate()

    private val contexts: MutableMap<Long, CommandExecutionContext> = ConcurrentHashMap()

    fun findByChatId(chatId: Long): CommandExecutionContext? {
        return contexts[chatId]
    }

    fun removeByChatId(chatId: Long) {
        contexts.remove(chatId)
    }

    fun create(chatId: Long, update: Update, commandName: String, synthetic: Boolean = false): CommandExecutionContext? {
        val command = commandRepository.findByName(commandName) ?: return null
        if (command.synthetic && !synthetic) {
            return null
        }
        if (command.admin) {
            val adminChatIds = adminChatIdsProvider?.adminChatIds()
            if (adminChatIds == null || chatId !in adminChatIds) {
                logger.warn("Attempted to execute admin command: ${update}.")
                return null
            }
        }
        val context = createCommandContext(chatId, update, command)
        val commandContext = CommandExecutionContext(context = context, command = command)
        contexts[chatId] = commandContext
        return commandContext
    }

    private fun createCommandContext(chatId: Long, update: Update, command: TelegramCommand<out Context>): Context {
        val context = command.createContext()
        context.firstName = update.message?.from?.firstName
                .orElse { update.callbackQuery?.from?.firstName!! }
        context.lastName = update.message?.from?.lastName
                .orElse { update.callbackQuery?.from?.lastName }
        context.chatId = chatId
        context.locale = update.message?.from?.languageCode?.run { Locale(this) }
        return context
    }
}