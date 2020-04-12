package com.sbrati.spring.boot.starter.kotlin.telegram.service

import com.sbrati.spring.boot.starter.kotlin.telegram.manager.TelegramResultProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

class UserAwarenessService(private val awarenessLevel: Int, private val message: Any) {

    constructor(builder: Builder) : this(builder.awarenessLevel!!, builder.message!!)

    @Autowired
    private lateinit var awareService: AwarenessService
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val resultProcessor: TelegramResultProcessor by lazy {
        applicationContext.getBean(TelegramResultProcessor::class.java)
    }

    fun aware() {
        val uninformedUserIds = awareService.findUninformedUserIds(awarenessLevel)
        uninformedUserIds.forEach { chatId ->
            resultProcessor.processResult(chatId, message)
            awareService.setUserInformLevel(chatId, awarenessLevel)
        }
    }

    class Builder {
        var awarenessLevel: Int? = null
        var message: Any? = null
    }
}

fun userAwarenessService(builder: UserAwarenessService.Builder.() -> Unit): UserAwarenessService {
    return UserAwarenessService(UserAwarenessService.Builder().apply(builder))
}