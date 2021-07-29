package com.sbrati.spring.boot.starter.kotlin.telegram.model


data class AnswerPreCheckoutQuery(
    val preCheckoutQueryId: String,
    val ok: Boolean = true,
    val errorMessageKey: String? = null,
    val errorMessageArgs: List<String> = emptyList()
)
