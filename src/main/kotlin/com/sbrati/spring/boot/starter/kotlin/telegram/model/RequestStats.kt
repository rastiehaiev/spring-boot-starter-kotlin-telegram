package com.sbrati.spring.boot.starter.kotlin.telegram.model

import com.fasterxml.jackson.annotation.JsonIgnore


class RequestStats(
    var banned: Boolean = false,
    var requestsCount: Int = 0,
    var justBanned: Boolean = false
) {

    @JsonIgnore
    fun trackRequest(): Int {
        return ++requestsCount
    }

    override fun toString(): String {
        return "RequestStats(banned=$banned, requestsCount=$requestsCount, justBanned=$justBanned)"
    }
}