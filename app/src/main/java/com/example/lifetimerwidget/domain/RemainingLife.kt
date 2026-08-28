package com.example.lifetimerwidget.domain

class RemainingLife(
    val years: Long,
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long
) {
    init {
        require(years >= 0) { "Years cannot be negative" }
        require(days in 0..365) { "Days must be between 0 and 365" }
        require(hours in 0..23) { "Hours must be between 0 and 23" }
        require(minutes in 0..59) { "Minutes must be between 0 and 59" }
        require(seconds in 0..59) { "Seconds must be between 0 and 59" }
    }
    
    val isExpired: Boolean
        get() = years == 0L && days == 0L && hours == 0L && minutes == 0L && seconds == 0L
}
