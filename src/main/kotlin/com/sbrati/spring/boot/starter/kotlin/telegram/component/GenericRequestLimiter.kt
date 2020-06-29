package com.sbrati.spring.boot.starter.kotlin.telegram.component

import com.sbrati.spring.boot.starter.kotlin.telegram.model.BanOptions
import com.sbrati.spring.boot.starter.kotlin.telegram.model.RequestStats
import com.sbrati.spring.boot.starter.kotlin.telegram.repository.RequestStatisticsRepository


class GenericRequestLimiter(
    val banOptions: BanOptions,
    private val repository: RequestStatisticsRepository
) {

    fun tryAcquire(chatId: Long): RequestStats {
        val requestStats = repository.getOrCreate(chatId, banOptions)
        if (requestStats.banned) {
            requestStats.justBanned = false
            repository.save(chatId, requestStats, banOptions)
            return requestStats
        }
        if (requestStats.trackRequest() <= banOptions.allowedRequestsPerMinute) {
            repository.save(chatId, requestStats, banOptions)
            return requestStats
        }

        requestStats.banned = true
        requestStats.justBanned = true
        repository.save(chatId, requestStats, banOptions)
        return requestStats
    }

    fun ban(chatId: Long) {
        val requestStats = repository.getOrCreate(chatId, banOptions)
        requestStats.banned = true
        requestStats.justBanned = true
        repository.save(chatId, requestStats, banOptions)
    }
}