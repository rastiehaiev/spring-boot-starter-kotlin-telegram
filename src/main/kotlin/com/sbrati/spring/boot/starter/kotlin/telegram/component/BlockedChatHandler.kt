package com.sbrati.spring.boot.starter.kotlin.telegram.component

interface BlockedChatHandler {

    fun onChatIdBlocked(chatId: Long)
}