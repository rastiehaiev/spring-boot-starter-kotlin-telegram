package com.sbrati.spring.boot.starter.kotlin.telegram.repository

import com.sbrati.spring.boot.starter.kotlin.telegram.model.BanOptions
import com.sbrati.spring.boot.starter.kotlin.telegram.model.RequestStats


interface RequestStatisticsRepository {

    fun getOrCreate(chatId: Long, banOptions: BanOptions): RequestStats

    fun save(chatId: Long, stats: RequestStats, banOptions: BanOptions)
}