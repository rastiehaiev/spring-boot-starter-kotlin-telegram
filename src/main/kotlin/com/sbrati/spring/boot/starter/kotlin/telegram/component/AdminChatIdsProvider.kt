package com.sbrati.spring.boot.starter.kotlin.telegram.component

interface AdminChatIdsProvider {

    fun adminChatIds(): List<Long>
}