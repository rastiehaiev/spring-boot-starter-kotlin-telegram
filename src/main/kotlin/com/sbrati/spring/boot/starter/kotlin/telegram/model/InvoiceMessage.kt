package com.sbrati.spring.boot.starter.kotlin.telegram.model


data class InvoiceMessage(
    val chatId: Long? = null,
    val payload: String,
    val titleKey: String,
    val titleArgs: List<String> = emptyList(),
    val descriptionKey: String,
    val descriptionArgs: List<String> = emptyList(),
    val currency: String,
    val price: Int,
    val photoUrl: String,
    val providerData: String,
    val providerToken: String
)