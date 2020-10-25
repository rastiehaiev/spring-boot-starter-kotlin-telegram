package com.sbrati.spring.boot.starter.kotlin.telegram.component

import com.sbrati.spring.boot.starter.kotlin.telegram.manager.TelegramManager
import com.sbrati.telegram.domain.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class SyntheticEventSender {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val telegramManager: TelegramManager by lazy {
        applicationContext.getBean(TelegramManager::class.java)
    }

    fun send(event: Event<*>) {
        telegramManager.onEvent(event)
    }
}