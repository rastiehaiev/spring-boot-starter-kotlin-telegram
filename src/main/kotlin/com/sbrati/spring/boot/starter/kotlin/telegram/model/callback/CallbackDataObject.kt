package com.sbrati.spring.boot.starter.kotlin.telegram.model.callback

abstract class CallbackDataObject {

    abstract fun getKey(): String

    abstract fun asString(): String

    abstract fun construct(value: String)
}