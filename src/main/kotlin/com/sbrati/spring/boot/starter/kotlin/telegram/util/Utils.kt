package com.sbrati.spring.boot.starter.kotlin.telegram.util

import me.ivmg.telegram.entities.Update
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject

class LoggerDelegate<in R : Any> : ReadOnlyProperty<R, Logger> {
    override fun getValue(thisRef: R, property: KProperty<*>): Logger =
            LoggerFactory.getLogger(getClassForLogging(thisRef.javaClass))
}

fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
    return javaClass.enclosingClass?.takeIf { it.kotlin.companionObject?.java == javaClass } ?: javaClass
}

fun <T> T?.orElse(t: T): T {
    return this ?: t
}

fun Update.chatId(): Long? {
    return this.message?.chat?.id ?: this.callbackQuery?.message?.chat?.id
}