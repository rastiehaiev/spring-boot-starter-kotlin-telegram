package com.sbrati.spring.boot.starter.kotlin.telegram.command

import java.util.*

open class Context {

    val uuid: UUID = UUID.randomUUID()
    lateinit var firstName: String

    var chatId: Long? = null
    var locale: Locale? = null
    var lastName: String? = null
}