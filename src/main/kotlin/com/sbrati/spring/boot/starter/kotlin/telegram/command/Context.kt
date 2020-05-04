package com.sbrati.spring.boot.starter.kotlin.telegram.command

import org.apache.commons.lang3.RandomStringUtils
import java.util.*

open class Context {

    val id: String = RandomStringUtils.randomAlphanumeric(5)
    lateinit var firstName: String

    var chatId: Long? = null
    var locale: Locale? = null
    var lastName: String? = null
}