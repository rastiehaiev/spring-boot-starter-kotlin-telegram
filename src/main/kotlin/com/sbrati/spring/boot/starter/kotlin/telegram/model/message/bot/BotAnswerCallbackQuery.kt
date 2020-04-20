package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

class BotAnswerCallbackQuery(val callbackQueryId: String,
                             val text: String? = null,
                             val showAlert: Boolean? = null,
                             val url: String? = null,
                             val cacheTime: Int? = null) : BotResultEntity