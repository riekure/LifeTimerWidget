package com.example.lifetimerwidget.domain

class LifeExpectancy(val years: Int) {
    init {
        require(years > 0) { "Life expectancy must be greater than 0" }
    }
}
