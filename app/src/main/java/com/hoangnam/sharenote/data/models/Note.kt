package com.hoangnam.sharenote.data.models

data class Note(
    var content: String? = null,
    var user: String? = null,
    val timeStamp: String? = null
)