package com.sbrati.spring.boot.starter.kotlin.telegram.operations

import com.sbrati.spring.boot.starter.kotlin.telegram.model.callback.CallbackDataObject
import me.ivmg.telegram.entities.Update
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class GlobalCallbackHandler<C : CallbackDataObject>(private val kClass: KClass<C>,
                                                    private val callbackData: String,
                                                    private val handler: (Update, C) -> Any) {

    fun isApplicable(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val receivedCallbackData = callbackQuery.data
        val callbackPrefix = "${globalCallbackPrefix()}$callbackData"
        return receivedCallbackData.startsWith(callbackPrefix)
    }

    fun handle(update: Update): Any {
        val receivedCallbackData = update.callbackQuery!!.data
        val cleanCallbackData = receivedCallbackData.removePrefix(globalCallbackPrefix())
        val callbackData = getCallbackDataObject(cleanCallbackData)
        return handler(update, callbackData)
    }

    private fun getCallbackDataObject(cleanCallbackData: String): C {
        val callbackData = kClass.primaryConstructor?.call()!!
        callbackData.construct(cleanCallbackData)
        return callbackData
    }

    private fun globalCallbackPrefix() = "GLOBAL^"
}