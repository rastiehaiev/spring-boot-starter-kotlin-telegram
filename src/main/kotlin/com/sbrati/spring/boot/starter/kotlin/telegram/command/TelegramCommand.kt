package com.sbrati.spring.boot.starter.kotlin.telegram.command

import com.sbrati.spring.boot.starter.kotlin.telegram.context.CommandContext

abstract class TelegramCommand<T : TelegramCommandProgress>(val name: String, val admin: Boolean = false) {

    private val stages: MutableList<TelegramCommandStage<T>> = ArrayList()

    fun stage(name: String, stageOperations: TelegramCommandStage<T>.() -> Unit) {
        stages.add(TelegramCommandStage<T>(name).apply(stageOperations))
    }

    fun getCurrentOrFirstStageName(context: CommandContext): String {
        if (context.currentStage == null) {
            return stages[0].name
        }
        return context.currentStage!!
    }

    fun getExistingStageName(stageName: String?): String? {
        return stageName?.run { findStageByNameNullable(this)?.name }
    }

    fun getNextStageName(context: CommandContext): String? {
        return findNextStageByName(context.currentStage!!)?.run { this.name }
    }

    fun findStageByName(name: String): TelegramCommandStage<T> {
        return findStageByNameNullable(name)!!
    }

    fun hasNoHandlers(): Boolean {
        return stages.flatMap { it.handlers }.count() + stages.flatMap { it.eventHandlers }.count() == 0
    }

    abstract fun createProgressEntity(): T

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