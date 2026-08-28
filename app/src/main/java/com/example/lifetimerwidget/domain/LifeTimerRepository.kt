package com.example.lifetimerwidget.domain

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate

class LifeTimerRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("life_timer_prefs", Context.MODE_PRIVATE)

    fun getBirthDate(): BirthDate {
        val dateString = prefs.getString("birth_date", "1994-01-04") ?: "1994-01-04"
        return try {
            BirthDate(LocalDate.parse(dateString))
        } catch (e: Exception) {
            BirthDate(LocalDate.of(1994, 1, 4))
        }
    }

    fun saveBirthDate(date: LocalDate) {
        prefs.edit().putString("birth_date", date.toString()).apply()
    }

    fun getLifeExpectancy(): LifeExpectancy {
        val years = prefs.getInt("life_expectancy", 81)
        return LifeExpectancy(years)
    }

    fun saveLifeExpectancy(years: Int) {
        prefs.edit().putInt("life_expectancy", years).apply()
    }
}
