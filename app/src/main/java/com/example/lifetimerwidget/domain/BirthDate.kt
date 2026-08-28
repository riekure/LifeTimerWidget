package com.example.lifetimerwidget.domain

import java.time.LocalDate

class BirthDate(val value: LocalDate) {
    init {
        require(!value.isAfter(LocalDate.now())) { "Birth date cannot be in the future" }
    }
}
