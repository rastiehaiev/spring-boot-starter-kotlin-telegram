package com.sbrati.spring.boot.starter.kotlin.telegram.handler.replyview

import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.RemoveReplyView
import me.ivmg.telegram.entities.ReplyKeyboardRemove
import me.ivmg.telegram.entities.ReplyMarkup
import org.springframework.stereotype.Component

@Component
class RemoveReplyViewResolver : AbstractReplyViewResolver<RemoveReplyView>(RemoveReplyView::class.java) {

    override fun handle(chatId: Long, resultPayload: RemoveReplyView): ReplyMarkup? {
        return ReplyKeyboardRemove()
    }
}