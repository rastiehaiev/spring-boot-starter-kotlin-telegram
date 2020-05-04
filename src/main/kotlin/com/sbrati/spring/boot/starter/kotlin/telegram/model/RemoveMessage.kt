package com.sbrati.spring.boot.starter.kotlin.telegram.model

class RemoveMessage(val messageId: Long)

fun removeMessage(messageId: Long): RemoveMessage {
    return RemoveMessage(messageId)
}