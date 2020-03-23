package com.sbrati.spring.boot.starter.kotlin.telegram.model

import com.sbrati.spring.boot.starter.kotlin.telegram.model.callback.CallbackDataObject

class InlineKeyboardButton(var key: String = "",
                           var args: List<String> = emptyList(),
                           var callbackData: CallbackDataObject,
                           var global: Boolean = false)