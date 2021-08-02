package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.payments.InvoicePhotoDetail
import com.github.kotlintelegrambot.entities.payments.InvoiceUserDetail
import com.github.kotlintelegrambot.entities.payments.LabeledPrice
import com.github.kotlintelegrambot.entities.payments.PaymentInvoiceInfo
import com.sbrati.spring.boot.starter.kotlin.telegram.model.InvoiceMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component


@Component
class InvoiceHandler(
    private val telegramMessageResolver: TelegramMessageResolver
) : ResultHandler<InvoiceMessage>(InvoiceMessage::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: InvoiceMessage) {
        val title = telegramMessageResolver.resolve(chatId, resultPayload.titleKey, resultPayload.titleArgs)
        val description = telegramMessageResolver.resolve(chatId, resultPayload.descriptionKey, resultPayload.descriptionArgs)
        bot.sendInvoice(
            chatId = ChatId.fromId(chatId),
            paymentInvoiceInfo = PaymentInvoiceInfo(
                title = title,
                description = description,
                payload = resultPayload.payload,
                providerData = resultPayload.providerData,
                providerToken = resultPayload.providerToken,
                startParameter = "",
                currency = resultPayload.currency,
                prices = listOf(
                    LabeledPrice(
                        label = "test label",
                        amount = resultPayload.price.toBigInteger()
                    )
                ),
                invoicePhoto = InvoicePhotoDetail(photoUrl = resultPayload.photoUrl, photoSize = 50),
                invoiceUserDetail = InvoiceUserDetail(needPhoneNumber = false, sendPhoneNumberToProvider = false)
            )
        )
    }
}