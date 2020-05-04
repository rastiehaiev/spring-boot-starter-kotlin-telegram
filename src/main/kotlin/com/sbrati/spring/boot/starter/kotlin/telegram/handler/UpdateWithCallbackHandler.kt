package com.sbrati.spring.boot.starter.kotlin.telegram.handler

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.sbrati.spring.boot.starter.kotlin.telegram.model.callback.CallbackDataObject
import me.ivmg.telegram.entities.Update
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class UpdateWithCallbackHandler<C : CallbackDataObject, T : Context>(private val kClass: KClass<C>,
                                                                     private val callbackData: String,
                                                                     private val handler: (Update, C, T) -> Any) : UpdateHandler<T>() {

    override fun isApplicable(update: Update, progress: T): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val receivedCallbackData = callbackQuery.data
        val callbackPrefix = "${progressUuidPrefix(progress)}$callbackData"
        return receivedCallbackData.startsWith(callbackPrefix)
    }

    override fun handle(update: Update, progress: T): Any {
        val receivedCallbackData = update.callbackQuery!!.data
        val cleanCallbackData = receivedCallbackData.removePrefix(progressUuidPrefix(progress))
        val callbackData = getCallbackDataObject(cleanCallbackData)
        return handler(update, callbackData, progress)
    }

    private fun getCallbackDataObject(cleanCallbackData: String): C {
        val callbackData = kClass.primaryConstructor?.call()!!
        callbackData.construct(cleanCallbackData)
        return callbackData
    }

    private fun progressUuidPrefix(progress: T) = "${progress.id}^"
}