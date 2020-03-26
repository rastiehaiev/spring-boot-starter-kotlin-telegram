package com.sbrati.spring.boot.starter.kotlin.telegram.manager.resulthandler

abstract class ResultHandler<R>(private val kClass: Class<R>) {

    fun isApplicable(resultPayload: Any): Boolean {
        return kClass.isInstance(resultPayload)
    }

    abstract fun handle(chatId: Long, resultPayload: R)
}