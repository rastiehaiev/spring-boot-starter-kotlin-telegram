package com.sbrati.spring.boot.starter.kotlin.telegram.controller

import com.sbrati.spring.boot.starter.kotlin.telegram.manager.TelegramManager
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import com.github.kotlintelegrambot.entities.Update
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@ConditionalOnProperty(name = ["telegram.mode"], havingValue = "WEBHOOK")
class TelegramBotController(private val telegramManager: TelegramManager) {

    private val logger by LoggerDelegate()

    init {
        logger.info("Created TelegramBotController to serve webhook requests.")
    }

    @PostMapping("/webhook")
    fun processMessage(@RequestBody update: Update) {
        telegramManager.onUpdate(update)
    }
}