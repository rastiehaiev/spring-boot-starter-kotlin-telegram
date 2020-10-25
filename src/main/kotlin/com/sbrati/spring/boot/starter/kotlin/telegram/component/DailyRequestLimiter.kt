package com.sbrati.spring.boot.starter.kotlin.telegram.component

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.EmptyMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate


abstract class DailyRequestLimiter {

    private val logger by LoggerDelegate()

    fun checkLimit(chatId: Long): Any? {
        val currentUsageCount = getCurrentUsageCount(chatId)
        logger.debug("[{}] Current usage count: {}.", chatId, currentUsageCount)
        val limitCount = getLimitCount()
        if (currentUsageCount > limitCount) {
            if (currentUsageCount - limitCount < getWarnUserCount()) {
                return errorResponse(chatId)
            }
            return EmptyMessage
        }
        return null
    }

    protected abstract fun getCurrentUsageCount(chatId: Long): Long

    protected abstract fun getLimitCount(): Int

    protected abstract fun getWarnUserCount(): Int

    protected abstract fun errorResponse(chatId: Long): Any
}