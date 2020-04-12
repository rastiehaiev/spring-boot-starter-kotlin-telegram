package com.sbrati.spring.boot.starter.kotlin.telegram.manager

import com.fasterxml.jackson.databind.ObjectMapper
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommand
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandProgress
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandStage
import com.sbrati.spring.boot.starter.kotlin.telegram.component.AdminChatIdsProvider
import com.sbrati.spring.boot.starter.kotlin.telegram.component.RequestLimiter
import com.sbrati.spring.boot.starter.kotlin.telegram.context.CommandContext
import com.sbrati.spring.boot.starter.kotlin.telegram.context.TelegramCommandContextRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.handler.EventHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.handler.UpdateHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.model.FinishWithMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.Message
import com.sbrati.spring.boot.starter.kotlin.telegram.model.NoHandlerFound
import com.sbrati.spring.boot.starter.kotlin.telegram.model.stages.JumpToStage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.stages.NextStage
import com.sbrati.spring.boot.starter.kotlin.telegram.operations.GlobalEventHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.operations.TelegramGlobalOperations
import com.sbrati.spring.boot.starter.kotlin.telegram.service.UserService
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import com.sbrati.spring.boot.starter.kotlin.telegram.util.chatId
import com.sbrati.telegram.domain.Event
import me.ivmg.telegram.entities.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class TelegramOperationsManager(private val contextRepository: TelegramCommandContextRepository,
                                private val commandRepository: TelegramCommandRepository,
                                private val objectMapper: ObjectMapper) {

    private val logger by LoggerDelegate()

    @Autowired(required = false)
    private var globalOperations: TelegramGlobalOperations? = null
    @Autowired
    private var userService: UserService<*>? = null
    @Autowired
    private var requestLimiter: RequestLimiter? = null
    @Autowired
    private var adminChatIdsProvider: AdminChatIdsProvider? = null

    fun onEvent(event: Event<*>): Any? {
        logger.debug("Received an event: {}", event)
        if (event.isGlobal) {
            return processGlobalEvent(event)
        }

        synchronized(event.chatId) {
            var context: CommandContext? = null
            var progress: TelegramCommandProgress? = null
            var command: TelegramCommand<out TelegramCommandProgress>? = null
            if (progress == null) {
                context = contextRepository.findByChatId(event.chatId)
                progress = context?.progress
                command = context?.run { commandRepository.findByName(this.commandName) }
            }

            if (command == null || context == null || progress == null) {
                return null
            }

            return when (val handleEvent = handleEventPayload(command, context, progress, event, command.getCurrentOrFirstStageName(context))) {
                is NextStage -> handleEventPayload(command, context, progress, event, command.getNextStageName(context))
                is JumpToStage -> handleEventPayload(command, context, progress, event, command.getExistingStageName(handleEvent.stage))
                else -> handleEvent
            }
        }
    }

    private fun processGlobalEvent(event: Event<*>): Any? {
        return globalOperations?.let {
            val eventHandler = it.eventHandlers.firstOrNull { handler -> handler.isApplicable(event.payload) } as GlobalEventHandler<Any>?
            return eventHandler?.handle(event as Event<Any>)
        }
    }

    private fun handleEventPayload(command: TelegramCommand<out TelegramCommandProgress>, context: CommandContext, progress: TelegramCommandProgress, event: Event<*>, stage: String?): Any? {
        stage ?: return null
        logger.debug("Processing event. Command: {}, stage: {}.", command.name, stage)
        val startStageResult = tryToStartStage(context, command, progress, stage)
        if (startStageResult != null) {
            return startStageResult
        }

        val currentStage = command.findStageByName(stage)
        val eventHandler: (EventHandler<Any, TelegramCommandProgress>)? = currentStage.eventHandlers.firstOrNull { it.isApplicable(event.payload) } as (EventHandler<Any, TelegramCommandProgress>)?
        return eventHandler?.handle(event as Event<Any>, progress) ?: NoHandlerFound.INSTANCE
    }

    fun onUpdate(update: Update): Any? {
        logger.debug("Received an update: {}", objectMapper.writeValueAsString(update))
        val chatId = update.chatId() ?: return null

        val localRequestLimiter = requestLimiter
        if (localRequestLimiter != null) {
            if (!localRequestLimiter.tryAcquire(chatId)) {
                if (localRequestLimiter.isJustBanned(chatId)) {
                    return globalOperations?.banHandler?.handle(update, localRequestLimiter.getBanOptions())
                }
                return null
            }
        }

        synchronized(chatId) {
            userService?.apply(update)

            val globalCallbackHandler = globalOperations?.let { it.handlers.firstOrNull { handler -> handler.isApplicable(update) } }
            if (globalCallbackHandler != null) {
                return globalCallbackHandler.handle(update)
            }

            var context: CommandContext? = null
            var progress: TelegramCommandProgress? = null
            var command: TelegramCommand<out TelegramCommandProgress>? = null

            // override current command if new command received
            val text = update.message?.text
            if (text != null && text.startsWith("/")) {
                val commandName = text.substring(1)
                command = commandRepository.findByName(commandName) ?: return null
                progress = createCommandProgress(command, update, chatId)

                context = contextRepository.create(chatId, command)
                context.progress = progress
            }

            // trying to find existing command by chat ID
            if (progress == null) {
                context = contextRepository.findByChatId(chatId)
                progress = context?.progress
                command = context?.run { commandRepository.findByName(this.commandName) }
            }

            if (command == null || context == null || progress == null) {
                return null
            }

            if (command.admin) {
                val adminChatIds = adminChatIdsProvider?.adminChatIds()
                if (adminChatIds == null || chatId !in adminChatIds) {
                    logger.warn("Failed to execute admin command: ${update}.")
                    return null
                }
            }

            return when (val handleUpdate = handleUpdate(command, context, progress, update, command.getCurrentOrFirstStageName(context))) {
                is NextStage -> handleUpdate(command, context, progress, update, command.getNextStageName(context))
                is JumpToStage -> handleUpdate(command, context, progress, update, command.getExistingStageName(handleUpdate.stage))
                else -> handleUpdate
            }
        }
    }

    private fun handleUpdate(command: TelegramCommand<out TelegramCommandProgress>,
                             context: CommandContext,
                             progress: TelegramCommandProgress,
                             update: Update,
                             stage: String?): Any? {

        stage ?: return null
        logger.debug("Processing update. Command: {}, stage: {}.", command.name, stage)
        val startStageResult = tryToStartStage(context, command, progress, stage)
        if (startStageResult != null) {
            return startStageResult
        }

        // trying to process update
        val currentStage = command.findStageByName(stage)
        return processUpdate(currentStage, update, progress)
    }

    private fun tryToStartStage(context: CommandContext, command: TelegramCommand<out TelegramCommandProgress>, progress: TelegramCommandProgress, stage: String): Any? {
        context.currentStageStarted = context.currentStage == stage
        context.currentStage = stage
        if (!context.currentStageStarted) {
            val currentStage = command.findStageByName(stage)
            val startMessage = startMessage(currentStage, progress)
            context.currentStageStarted = true
            if (startMessage != null) {
                if (command.hasNoHandlers()) {
                    return FinishWithMessage(startMessage)
                }
                return startMessage
            }
        }
        return null
    }

    private fun createCommandProgress(command: TelegramCommand<out TelegramCommandProgress>, update: Update, chatId: Long): TelegramCommandProgress? {
        val progress = command.createProgressEntity()
        progress.firstName = update.message!!.from!!.firstName
        progress.lastName = update.message!!.from!!.lastName
        progress.chatId = chatId
        progress.locale = update.message?.from?.languageCode?.run { Locale(this) }
        return progress
    }

    private fun processUpdate(currentStage: TelegramCommandStage<out TelegramCommandProgress>, update: Update, progress: TelegramCommandProgress): Any? {
        val handler: UpdateHandler<TelegramCommandProgress>? = currentStage.handlers.firstOrNull {
            val updateHandler: UpdateHandler<TelegramCommandProgress> = it as (UpdateHandler<TelegramCommandProgress>)
            updateHandler.isApplicable(update, progress)
        } as (UpdateHandler<TelegramCommandProgress>?)
        return handler?.handle(update, progress) ?: NoHandlerFound.INSTANCE
    }

    private fun startMessage(currentStage: TelegramCommandStage<out TelegramCommandProgress>, progress: TelegramCommandProgress): Message? {
        val startMessage: ((TelegramCommandProgress) -> Message)? = currentStage.startMessage as ((TelegramCommandProgress) -> Message)?
        if (startMessage != null) {
            return startMessage.invoke(progress)
        }
        return null
    }
}