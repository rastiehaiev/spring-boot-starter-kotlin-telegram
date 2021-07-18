package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.message

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.network.Response
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

abstract class BotHandler<R>(private val kClass: Class<R>) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    fun isApplicable(resultPayload: Any): Boolean {
        return kClass.isInstance(resultPayload)
    }

    abstract fun handle(botMessage: R): BotResult
}

fun Pair<retrofit2.Response<Response<Message>?>?, Exception?>.getCode(): Int? {
    return this.first?.raw()?.code()
}