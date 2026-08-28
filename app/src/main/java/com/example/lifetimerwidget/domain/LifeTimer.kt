package com.example.lifetimerwidget.domain

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class LifeTimer(
    private val birthDate: BirthDate,
    private val lifeExpectancy: LifeExpectancy
) {
    fun calculateRemainingLife(currentDateTime: LocalDateTime): RemainingLife {
        val deathDateTime = birthDate.value.atStartOfDay().plusYears(lifeExpectancy.years.toLong())
        
        if (currentDateTime.isAfter(deathDateTime) || currentDateTime.isEqual(deathDateTime)) {
            return RemainingLife(0, 0, 0, 0, 0)
        }

        var tempDateTime = currentDateTime
        val years = ChronoUnit.YEARS.between(tempDateTime, deathDateTime)
        tempDateTime = tempDateTime.plusYears(years)

        val days = ChronoUnit.DAYS.between(tempDateTime, deathDateTime)
        tempDateTime = tempDateTime.plusDays(days)

        val hours = ChronoUnit.HOURS.between(tempDateTime, deathDateTime)
        tempDateTime = tempDateTime.plusHours(hours)

        val minutes = ChronoUnit.MINUTES.between(tempDateTime, deathDateTime)
        tempDateTime = tempDateTime.plusMinutes(minutes)

        val seconds = ChronoUnit.SECONDS.between(tempDateTime, deathDateTime)

        return RemainingLife(years, days, hours, minutes, seconds)
    }
}
