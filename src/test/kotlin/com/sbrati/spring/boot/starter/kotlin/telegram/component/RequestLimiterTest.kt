package com.sbrati.spring.boot.starter.kotlin.telegram.component

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

internal class RequestLimiterTest {

    @Test
    fun `Should acquire when limit is not exceeded`() {
        val chatId = 1L
        val requestLimiter = RequestLimiter(allowedRequestsPerMinute = 10, banDurationSeconds = 5)

        assertTrue(requestLimiter.tryAcquire(chatId))
        assertTrue(requestLimiter.tryAcquire(chatId))
        assertTrue(requestLimiter.tryAcquire(chatId))
        assertTrue(requestLimiter.tryAcquire(chatId))
    }

    @Test
    fun `Should not acquire when limit is exceeded`() {
        val chatId = 1L
        val requestLimiter = RequestLimiter(allowedRequestsPerMinute = 2, banDurationSeconds = 5)

        assertTrue(requestLimiter.tryAcquire(chatId))
        assertTrue(requestLimiter.tryAcquire(chatId))
        assertFalse(requestLimiter.tryAcquire(chatId))
    }

    @Test
    fun `Should acquire when ban has expired`() {
        val banDurationSeconds: Long = 5

        val chatId = 1L
        val requestLimiter = RequestLimiter(allowedRequestsPerMinute = 2, banDurationSeconds = banDurationSeconds)

        assertTrue(requestLimiter.tryAcquire(chatId))
        assertTrue(requestLimiter.tryAcquire(chatId))
        assertFalse(requestLimiter.tryAcquire(chatId))

        Thread.sleep(TimeUnit.SECONDS.toMillis(banDurationSeconds + 1))

        assertTrue(requestLimiter.tryAcquire(chatId))
    }
}