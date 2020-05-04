package com.sbrati.spring.boot.starter.kotlin.telegram.model

class MultipleResults {

    val list: MutableList<Any> = mutableListOf()

    fun add(result: Any): MultipleResults {
        list.add(result)
        return this
    }
}

fun Any.addTo(results: MultipleResults) {
    results.list.add(this)
}