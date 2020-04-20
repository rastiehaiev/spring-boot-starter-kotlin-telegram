package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

class AnswerCallbackQuery : MessageSpec {

    lateinit var callbackQueryId: String
    lateinit var key: String
    var args: List<String> = emptyList()
    var showAlert: Boolean = false
}

fun answerCallbackQuery(builder: AnswerCallbackQuery.() -> Unit): AnswerCallbackQuery {
    return AnswerCallbackQuery().apply(builder)
}