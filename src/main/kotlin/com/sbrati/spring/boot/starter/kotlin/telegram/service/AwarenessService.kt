package com.sbrati.spring.boot.starter.kotlin.telegram.service

interface AwarenessService {

    fun findUninformedUserIds(informLevel: Int): List<Long>

    fun setUserInformLevel(chatId: Long, informLevel: Int)
}