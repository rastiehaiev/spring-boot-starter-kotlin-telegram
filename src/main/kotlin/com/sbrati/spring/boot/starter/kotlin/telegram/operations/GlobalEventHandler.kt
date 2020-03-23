package com.sbrati.spring.boot.starter.kotlin.telegram.operations

import com.sbrati.telegram.domain.Event
import kotlin.reflect.KClass

class GlobalEventHandler<P>(private val type: KClass<*>, private val handler: (Event<P>) -> Any) {

    fun isApplicable(any: Any?): Boolean {
        return type.isInstance(any)
    }

    fun handle(event: Event<P>): Any {
        return handler(event)
    }
}