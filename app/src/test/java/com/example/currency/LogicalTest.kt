package com.example.currency

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.abs
import kotlin.math.log10

class LogicalTest {
    @Test
    fun validatePinCode() {
        val input = 887712
        var isPinCodeValidated = true
        if (input.length() < 6) {
            isPinCodeValidated = false
        } else {
            val inputStr = input.toString()
            var next: String
            var prev = ""
            var countOne = 1
            var countTwo = 1
            var countThree = 1
            var countFour = 0
            var countFive = 0

            for (i in 0 ..< input.length()) {
                val char = inputStr[i].toString()

                // Check no more than repeating numbers
                next = char
                if (next == prev) countOne += 1
                else countOne = 1
                if (countOne > 2) {
                    isPinCodeValidated = false
                    break
                }

                // Check no more than 2 sequential numbers
                if (prev == "") prev = "1"
                if ((next.toInt() - (prev.toInt()) == 1)) countTwo += 1
                else countTwo = 1
                if (countTwo > 2) {
                    isPinCodeValidated = false
                    break
                }

                if ((prev.toInt() - next.toInt()) == 1) countThree += 1
                else countThree = 1
                if (countThree > 2) {
                    isPinCodeValidated = false
                    break
                }

                // Check no more than 2 sets of repeating numbers
                if (countOne == 1) countFive += 1
                if (countOne == 2) {
                    countFour += 1
                    countFive = 0
                }
                if (countFive == 2) countFour = 0
                if (countFour > 2) {
                    isPinCodeValidated = false
                    break
                }

                prev = next
            }
        }
        assertEquals(true, isPinCodeValidated)
    }

    private fun Int.length() = when(this) {
        0 -> 1
        else -> log10(abs(toFloat())).toInt() + 1
    }
}