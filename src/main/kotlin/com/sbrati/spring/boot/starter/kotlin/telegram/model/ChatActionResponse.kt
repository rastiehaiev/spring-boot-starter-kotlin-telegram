package com.sbrati.spring.boot.starter.kotlin.telegram.model

import me.ivmg.telegram.entities.ChatAction

class ChatActionResponse(val chatAction: ChatAction)

fun typing(): ChatActionResponse {
    return ChatActionResponse(ChatAction.TYPING)
}