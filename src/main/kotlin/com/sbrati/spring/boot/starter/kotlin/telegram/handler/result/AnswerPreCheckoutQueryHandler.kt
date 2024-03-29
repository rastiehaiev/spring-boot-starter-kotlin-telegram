package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.sbrati.spring.boot.starter.kotlin.telegram.model.AnswerPreCheckoutQuery
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component


@Component
class AnswerPreCheckoutQueryHandler(
    private val telegramMessageResolver: TelegramMessageResolver
) : ResultHandler<AnswerPreCheckoutQuery>(AnswerPreCheckoutQuery::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val log by LoggerDelegate()

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: AnswerPreCheckoutQuery) {
        val errorMessageKey = resultPayload.errorMessageKey
        val errorMessage = if (errorMessageKey != null) {
            telegramMessageResolver.resolve(chatId, errorMessageKey, resultPayload.errorMessageArgs)
        } else {
            null
        }

        log.info("Answering pre-checkout query.")
        bot.answerPreCheckoutQuery(resultPayload.preCheckoutQueryId, resultPayload.ok, errorMessage)
            .fold(
                ifSuccess = { success ->
                    log.info("Successfully answered pre-checkout: $success.")
                },
                ifError = { error ->
                    log.info("Error while answering pre-checkout: $error.")
                }
            )
    }
}