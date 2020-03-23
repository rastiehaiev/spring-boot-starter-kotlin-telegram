package com.sbrati.spring.boot.starter.kotlin.telegram.model

class FinishWithMessage(val message: Message)

fun finish(operation: () -> Message): FinishWithMessage {
    return FinishWithMessage(operation())
}