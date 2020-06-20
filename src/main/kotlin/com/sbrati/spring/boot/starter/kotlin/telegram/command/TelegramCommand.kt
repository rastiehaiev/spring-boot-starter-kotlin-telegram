package com.sbrati.spring.boot.starter.kotlin.telegram.command

import com.sbrati.spring.boot.starter.kotlin.telegram.context.CommandExecutionContext

abstract class TelegramCommand<T : Context>(val name: String, private val contextType: Class<T>, val admin: Boolean = false, val synthetic: Boolean = false) {

    private val stages: MutableList<TelegramCommandStage<T>> = ArrayList()

    fun stage(name: String, stageOperations: TelegramCommandStage<T>.() -> Unit) {
        stages.add(TelegramCommandStage<T>(name).apply(stageOperations))
    }

    fun getCurrentOrFirstStageName(executionContext: CommandExecutionContext): String {
        if (executionContext.currentStage == null) {
            return stages[0].name
        }
        return executionContext.currentStage!!
    }

    fun getExistingStageName(stageName: String?): String? {
        return stageName?.run { findStageByNameNullable(this)?.name }
    }

    fun getNextStageName(executionContext: CommandExecutionContext): String? {
        return findNextStageByName(executionContext.currentStage!!)?.run { this.name }
    }

    fun findStageByName(name: String): TelegramCommandStage<T> {
        return findStageByNameNullable(name)!!
    }

    fun createContext(): T = contextType.newInstance()

    private fun findStageByNameNullable(name: String): TelegramCommandStage<T>? {
        return stages.find { it.name == name }
    }

    private fun findNextStageByName(stage: String): TelegramCommandStage<T>? {
        val iterator = stages.iterator()
        while (iterator.hasNext()) {
            val currentElement = iterator.next()
            if (currentElement.name == stage) {
                if (iterator.hasNext()) {
                    return iterator.next()
                }
            }
        }
        return null
    }
}