package com.sbrati.spring.boot.starter.kotlin.telegram.service

import me.ivmg.telegram.entities.Update

interface UserService<U> {

    fun findByChatId(chatId: Long): U?

    fun saveOrUpdate(user: U)

    fun apply(update: Update)

    fun getAllChatIds(): List<Long>
}