package com.sbrati.spring.boot.starter.kotlin.telegram.properties

import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "telegram", ignoreUnknownFields = true)
class TelegramBotConfigurationProperties {
    lateinit var token: String
    lateinit var mode: TelegramBotMode
    var timeoutSeconds: Int = 10
    var logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC
}