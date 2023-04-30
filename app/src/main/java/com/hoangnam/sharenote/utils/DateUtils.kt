package com.hoangnam.sharenote.utils

import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {
    fun convertTimestampToString(s: String): String? {
        return try {
            val date = Date(s.toLong())
            val format = SimpleDateFormat("HH:mm dd/MM/yyyy")
            format.format(date)
        } catch (e: Exception) {
            e.toString()
        }
    }
}