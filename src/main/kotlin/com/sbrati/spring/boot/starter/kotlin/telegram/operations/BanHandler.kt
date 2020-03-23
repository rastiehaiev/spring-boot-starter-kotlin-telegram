package com.sbrati.spring.boot.starter.kotlin.telegram.operations

import com.sbrati.spring.boot.starter.kotlin.telegram.model.BanOptions
import me.ivmg.telegram.entities.Update

class BanHandler(private val handler: (Update, BanOptions) -> Any) {

    fun handle(update: Update, options: BanOptions): Any {
        return handler(update, options)
    }
}