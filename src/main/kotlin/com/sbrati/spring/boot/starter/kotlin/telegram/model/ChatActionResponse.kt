package com.sbrati.spring.boot.starter.kotlin.telegram.model

import com.github.kotlintelegrambot.entities.ChatAction

class ChatActionResponse(val chatAction: ChatAction)

fun typing(): ChatActionResponse {
    return ChatActionResponse(ChatAction.TYPING)
}