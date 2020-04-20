package com.sbrati.spring.boot.starter.kotlin.telegram.model

class FinishWith(val result: Any)

fun finish(operation: () -> Any): FinishWith {
    return FinishWith(operation())
}