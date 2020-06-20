package com.sbrati.spring.boot.starter.kotlin.telegram.command

import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.apache.commons.lang3.RandomStringUtils
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
open class Context {

    val id: String = RandomStringUtils.randomAlphanumeric(5)

    var chatId: Long? = null
    var locale: Locale? = null
    lateinit var firstName: String
    var lastName: String? = null
}