package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResult
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.getBotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotVideoNote
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.stereotype.Component

@Component
class BotVideoNoteHandler : BotHandler<BotVideoNote>(BotVideoNote::class.java) {

    override fun handle(botMessage: BotVideoNote): BotResult {
        val chatId = botMessage.chatId
        val result = bot.sendVideoNote(
                chatId = chatId,
                videoNoteId = botMessage.videoNoteId,
                replyMarkup = botMessage.replyMarkup)

        val status = result.getCode()?.getBotResultStatus().orElse(BotResultStatus.OK)
        return BotResult(status, chatId)
    }
}