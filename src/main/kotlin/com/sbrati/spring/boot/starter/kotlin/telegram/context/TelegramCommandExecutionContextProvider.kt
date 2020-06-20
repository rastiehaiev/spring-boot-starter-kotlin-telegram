package com.sbrati.spring.boot.starter.kotlin.telegram.context

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.sbrati.spring.boot.starter.kotlin.telegram.component.AdminChatIdsProvider
import com.sbrati.spring.boot.starter.kotlin.telegram.repository.TelegramCommandExecutionContextRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.repository.TelegramCommandRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import me.ivmg.telegram.entities.Update
import org.springframework.stereotype.Component
import java.util.*

@Component
class TelegramCommandExecutionContextProvider(private val commandRepository: TelegramCommandRepository,
                                              private var adminChatIdsProvider: AdminChatIdsProvider?,
                                              private val commandExecutionContextRepository: TelegramCommandExecutionContextRepository) {

    private val logger by LoggerDelegate()

    fun findByChatId(chatId: Long): CommandExecutionContext? {
        return commandExecutionContextRepository.retrieveAndLock(chatId)
    }

    fun removeByChatId(chatId: Long) {
        commandExecutionContextRepository.delete(chatId)
    }

    fun create(chatId: Long, update: Update, commandName: String, context: Context? = null, synthetic: Boolean = false): CommandExecutionContext? {
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
        val finalContext = (context ?: command.createContext()).fill(chatId, update)
        val commandContext = CommandExecutionContext(context = finalContext, commandName = commandName)
        if (synthetic) {
            commandExecutionContextRepository.store(commandContext)
        } else {
            commandExecutionContextRepository.storeAndAcquire(commandContext)
        }
        return commandContext
    }

    fun store(executionContext: CommandExecutionContext) {
        commandExecutionContextRepository.storeAndRelease(executionContext)
    }

    private fun Context.fill(chatId: Long, update: Update): Context {
        this.firstName = update.message?.from?.firstName.orElse { update.callbackQuery?.from?.firstName!! }
        this.lastName = update.message?.from?.lastName.orElse { update.callbackQuery?.from?.lastName }
        this.locale = update.message?.from?.languageCode?.run { Locale(this) }
        this.chatId = chatId
        return this
    }
}