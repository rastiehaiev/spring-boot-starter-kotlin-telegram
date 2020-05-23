package com.sbrati.spring.boot.starter.kotlin.telegram.operations

import me.ivmg.telegram.entities.Update

open abstract class GlobalUpdateHandler {

    abstract fun onUpdate(update: Update)
}