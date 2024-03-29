package com.sbrati.spring.boot.starter.kotlin.telegram.handler.replyview

import com.github.kotlintelegrambot.entities.ReplyMarkup

abstract class AbstractReplyViewResolver<R>(private val kClass: Class<R>) {

    fun isApplicable(resultPayload: Any): Boolean {
        return kClass.isInstance(resultPayload)
    }

    abstract fun handle(chatId: Long, resultPayload: R): ReplyMarkup?
}