package com.sbrati.spring.boot.starter.kotlin.telegram.component

import com.sbrati.spring.boot.starter.kotlin.telegram.model.BanOptions
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class RequestLimiter {

    private val allowedRequestsPerMinute: Int
    private val banDurationMillis: Long
    private val banOptions: BanOptions
    private val userPermits: MutableMap<Long, UserPermit>
    private val onBanAction: ((Long) -> Unit)?

    constructor(allowedRequestsPerMinute: Int, banDurationSeconds: Long, onBanAction: ((Long) -> Unit)? = null) {
        this.banOptions = BanOptions(allowedRequestsPerMinute, banDurationSeconds)
        if (allowedRequestsPerMinute <= 0 || banDurationSeconds <= 0) {
            throw IllegalStateException("Incorrect options specified for RequestLimiter: $banOptions.")
        }
        this.allowedRequestsPerMinute = allowedRequestsPerMinute
        this.banDurationMillis = TimeUnit.SECONDS.toMillis(banDurationSeconds)
        this.userPermits = ConcurrentHashMap()
        this.onBanAction = onBanAction
    }

    fun tryAcquire(chatId: Long): Boolean {
        val currentTimestamp = System.currentTimeMillis()
        val userPermit = userPermits.computeIfAbsent(chatId) { UserPermit() }
        val currentRequestsCount = userPermit.requestsCount.incrementAndGet()
        if (currentRequestsCount <= allowedRequestsPerMinute) {
            return true
        }

        synchronized(chatId) {
            if (!userPermit.banned) {
                return if (currentTimestamp - userPermit.creationTimestamp > TimeUnit.MINUTES.toMillis(1)) {
                    userPermit.requestsCount.set(0)
                    userPermit.creationTimestamp = System.currentTimeMillis()
                    true
                } else {
                    userPermit.banned = true
                    userPermit.justBanned.set(true)
                    userPermit.creationTimestamp = System.currentTimeMillis()
                    onBanAction?.invoke(chatId)
                    false
                }
            }

            if (currentTimestamp - userPermit.creationTimestamp > banDurationMillis) {
                userPermit.banned = false
                userPermit.requestsCount.set(0)
                userPermit.creationTimestamp = System.currentTimeMillis()
                return true
            }
            return false
        }
    }

    fun isJustBanned(chatId: Long): Boolean {
        return userPermits[chatId]?.justBanned?.getAndSet(false).orElse(false)
    }

    fun getBanOptions(): BanOptions {
        return banOptions
    }
}

private class UserPermit(@Volatile var banned: Boolean = false,
                         var requestsCount: AtomicInteger = AtomicInteger(0),
                         @Volatile var creationTimestamp: Long = System.currentTimeMillis(),
                         @Volatile var justBanned: AtomicBoolean = AtomicBoolean(false))