package com.sbrati.spring.boot.starter.kotlin.telegram.model.callback

class StringCallbackData : CallbackDataObject() {

    private lateinit var value: String

    override fun getKey(): String {
        return value
    }

    override fun asString(): String {
        return value
    }

    override fun construct(value: String) {
        this.value = value
    }
}

fun stringCallback(value: String): StringCallbackData {
    val stringCallbackData = StringCallbackData()
    stringCallbackData.construct(value)
    return stringCallbackData
}