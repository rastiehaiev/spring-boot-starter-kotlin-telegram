package com.sbrati.spring.boot.starter.kotlin.telegram.handler.message

import com.sbrati.spring.boot.starter.kotlin.telegram.handler.replyview.AbstractReplyViewResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.manager.resulthandler.ResultHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.MessageSpec
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.*
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.NoReplyView
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.ParseMode
import me.ivmg.telegram.entities.ReplyKeyboardRemove
import me.ivmg.telegram.entities.ReplyMarkup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

abstract class AbstractMessageHandler<M : MessageSpec>(type: Class<M>) : ResultHandler<M>(type) {

    @Autowired
    private var replyViewResolvers: List<AbstractReplyViewResolver<*>> = emptyList()

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: M) {
        val messages = messages(chatId, resultPayload)
        for (entity in messages) {
            when (entity) {
                is BotMessage -> bot.sendMessage(chatId = entity.chatId,
                        text = entity.text,
                        parseMode = entity.parseMode,
                        replyMarkup = entity.replyMarkup)
                is BotAnswerCallbackQuery -> bot.answerCallbackQuery(
                        callbackQueryId = entity.callbackQueryId,
                        url = entity.url,
                        text = entity.text,
                        showAlert = entity.showAlert,
                        cacheTime = entity.cacheTime)
                is BotPhoto -> bot.sendPhoto(chatId = entity.chatId,
                        photo = entity.photo,
                        parseMode = ParseMode.MARKDOWN,
                        replyMarkup = entity.replyMarkup,
                        caption = entity.caption)
                is BotVoice -> bot.sendVoice(chatId = entity.chatId,
                        audioId = entity.audioId,
                        replyMarkup = entity.replyMarkup)
                is BotVideoNote -> bot.sendVideoNote(
                        chatId = entity.chatId,
                        videoNoteId = entity.videoNoteId,
                        replyMarkup = entity.replyMarkup)
                is BotVideo -> bot.sendVideo(chatId = entity.chatId,
                        fileId = entity.fileId,
                        caption = entity.caption,
                        replyMarkup = entity.replyMarkup)
            }
        }
    }

    protected fun replyMarkup(chatId: Long, replyView: ReplyView?): ReplyMarkup? {
        if (replyView is NoReplyView) {
            return null
        }
        return replyView?.let {
            val resolver: AbstractReplyViewResolver<Any>? = replyViewResolvers.firstOrNull { it.isApplicable(replyView) } as AbstractReplyViewResolver<Any>?
            resolver?.handle(chatId, replyView)
        }.orElse(ReplyKeyboardRemove())
    }

    protected abstract fun messages(chatId: Long, message: M): List<BotResultEntity>
}