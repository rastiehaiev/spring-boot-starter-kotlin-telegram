package com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview

import com.sbrati.spring.boot.starter.kotlin.telegram.model.callback.CallbackDataObject

class InlineKeyboardButton(var key: String = "",
                           var args: List<String> = emptyList(),
                           var callbackData: CallbackDataObject? = null,
                           var url: String? = null,
                           var global: Boolean = false)