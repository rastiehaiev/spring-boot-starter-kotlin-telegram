package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResult
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.getBotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotPhoto
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import me.ivmg.telegram.entities.ParseMode
import org.springframework.stereotype.Component

@Component
class BotPhotoHandler : BotHandler<BotPhoto>(BotPhoto::class.java) {

    override fun handle(botMessage: BotPhoto): BotResult {
        val chatId = botMessage.chatId
        val result = bot.sendPhoto(chatId = chatId,
                photo = botMessage.photo,
                parseMode = ParseMode.MARKDOWN,
                replyMarkup = botMessage.replyMarkup,
                caption = botMessage.caption)

        val status = result.getCode()?.getBotResultStatus().orElse(BotResultStatus.OK)
        return BotResult(status, chatId)
    }
}