package com.sbrati.spring.boot.starter.kotlin.telegram.manager

import com.fasterxml.jackson.databind.ObjectMapper
import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandStage
import com.sbrati.spring.boot.starter.kotlin.telegram.component.RequestLimiter
import com.sbrati.spring.boot.starter.kotlin.telegram.context.CommandExecutionContext
import com.sbrati.spring.boot.starter.kotlin.telegram.context.TelegramCommandExecutionContextProvider
import com.sbrati.spring.boot.starter.kotlin.telegram.handler.event.EventHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.handler.update.UpdateHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.model.EmptyResult
import com.sbrati.spring.boot.starter.kotlin.telegram.model.NoHandlerFound
import com.sbrati.spring.boot.starter.kotlin.telegram.model.StartNewCommand
import com.sbrati.spring.boot.starter.kotlin.telegram.model.stages.JumpToStage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.stages.NextStage
import com.sbrati.spring.boot.starter.kotlin.telegram.operations.GlobalEventHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.operations.GlobalUpdateHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.operations.TelegramGlobalOperations
import com.sbrati.spring.boot.starter.kotlin.telegram.service.UserService
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import com.sbrati.spring.boot.starter.kotlin.telegram.util.chatId
import com.sbrati.telegram.domain.Event
import me.ivmg.telegram.entities.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TelegramOperationsManager(private val executionContextProvider: TelegramCommandExecutionContextProvider,
                                private val objectMapper: ObjectMapper) {

    private val logger by LoggerDelegate()

    @Autowired
    private var userService: UserService<*>? = null

    @Autowired
    private var requestLimiter: RequestLimiter? = null

    @Autowired(required = false)
    private var globalOperations: TelegramGlobalOperations? = null

    @Autowired(required = false)
    private var globalUpdateHandler: GlobalUpdateHandler? = null

    fun onEvent(event: Event<*>): Any? {
        logger.debug("Received an event: {}", event)
        if (event.isGlobal) {
            return processGlobalEvent(event)
        }

        synchronized(event.chatId) {
            val executionContext = executionContextProvider.findByChatId(event.chatId) ?: return null
            return when (val handleEvent = handleEventPayload(executionContext, event, executionContext.command.getCurrentOrFirstStageName(executionContext))) {
                is NextStage -> handleEventPayload(executionContext, event, executionContext.command.getNextStageName(executionContext))
                is JumpToStage -> handleEventPayload(executionContext, event, executionContext.command.getExistingStageName(handleEvent.stage))
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

    private fun handleEventPayload(executionContext: CommandExecutionContext, event: Event<*>, stage: String?): Any? {
        stage ?: return null
        logger.debug("Processing event. Command: {}, stage: {}.", executionContext.command.name, stage)
        val startStageResult = tryToStartStage(executionContext, stage)
        if (startStageResult != null) {
            return startStageResult
        }

        val currentStage = executionContext.command.findStageByName(stage)
        val eventHandler: (EventHandler<Any, Context>)? = currentStage.eventHandlers.firstOrNull { it.isApplicable(event.payload) } as (EventHandler<Any, Context>)?
        return eventHandler?.handle(event as Event<Any>, executionContext.context) ?: NoHandlerFound
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

        val globalUpdateHandler = this.globalUpdateHandler
        if (globalUpdateHandler != null) {
            globalUpdateHandler.onUpdate(update)
            return EmptyResult
        }

        synchronized(chatId) {
            userService?.apply(update)

            val globalCallbackHandler = globalOperations?.let { it.handlers.firstOrNull { handler -> handler.isApplicable(update) } }
            if (globalCallbackHandler != null) {
                val result = globalCallbackHandler.handle(update)
                if (result is StartNewCommand) {
                    executionContextProvider.create(chatId, update, result.commandName, synthetic = true)
                } else {
                    return result
                }
            }

            val text = update.message?.text
            val executionContext = (if (text != null && text.startsWith("/")) {
                val commandName = text.substring(1)
                executionContextProvider.create(chatId, update, commandName)
            } else {
                executionContextProvider.findByChatId(chatId)
            }) ?: return null

            return when (val handleUpdate = handleUpdate(executionContext, update, executionContext.command.getCurrentOrFirstStageName(executionContext))) {
                is NextStage -> handleUpdate(executionContext, update, executionContext.command.getNextStageName(executionContext))
                is JumpToStage -> handleUpdate(executionContext, update, executionContext.command.getExistingStageName(handleUpdate.stage))
                else -> handleUpdate
            }
        }
    }

    private fun handleUpdate(executionContext: CommandExecutionContext, update: Update, stage: String?): Any? {
        val command = executionContext.command
        stage ?: return null
        logger.debug("Processing update. Command: {}, stage: {}.", executionContext.command.name, stage)
        val startStageResult = tryToStartStage(executionContext, stage, update)
        if (startStageResult != null) {
            return startStageResult
        }

        // trying to process update
        val currentStage = command.findStageByName(stage)
        return processUpdate(currentStage, update, executionContext.context)
    }

    private fun tryToStartStage(executionContext: CommandExecutionContext, stage: String, update: Update? = null): Any? {
        val command = executionContext.command
        val context = executionContext.context
        executionContext.currentStageStarted = executionContext.currentStage == stage
        executionContext.currentStage = stage
        if (!executionContext.currentStageStarted) {
            val currentStage = command.findStageByName(stage)
            val startPayload = start(currentStage, context, update)
            executionContext.currentStageStarted = true
            if (startPayload != null) {
                return startPayload
            }
        }
        return null
    }

    private fun processUpdate(currentStage: TelegramCommandStage<out Context>, update: Update, context: Context): Any? {
        val handler: UpdateHandler<Context>? = currentStage.handlers.firstOrNull {
            val updateHandler: UpdateHandler<Context> = it as (UpdateHandler<Context>)
            updateHandler.isApplicable(update, context)
        } as (UpdateHandler<Context>?)
        logger.debug("Current stage: {}. Handler: {}.", currentStage.name, handler)
        return handler?.handle(update, context) ?: NoHandlerFound
    }

    private fun start(currentStage: TelegramCommandStage<out Context>, progress: Context, update: Update? = null): Any? {
        val start: ((Update?, Context) -> Any)? = currentStage.start as ((Update?, Context) -> Any)?
        if (start != null) {
            return start.invoke(update, progress)
        }
        return null
    }
}