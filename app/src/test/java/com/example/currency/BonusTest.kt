package com.example.currency

import org.junit.Test
import java.math.BigDecimal

class BonusTest {

    @Test
    fun filterArrayTwoSets() {
        var listOne = listOf(1,3,5,7,9,11,13,15,17,19,21,23,25)
        val listTwo = listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)
        val newList = mutableListOf<Int>()

        first@ for (i in listOne) {
            second@ for (j in listTwo) {
                if (i == j) {
                    newList.add(i)
                    break@second
                }
            }
        }
        listOne = newList
        println("filterArrayTwoSets listOne: $listOne")
    }

    @Test
    fun generateFibonacci() {
        val listFibonacci = mutableListOf<BigDecimal>()
        val integer = 50
        for (i in 1..integer) {
            if (listFibonacci.size < 1) {
                listFibonacci.add(BigDecimal(0))
            } else {
                val last = listFibonacci.last()
                val lastPrev = if (listFibonacci.size > 1) listFibonacci[listFibonacci.size - 2] else BigDecimal(1)
                val new = last.plus(lastPrev)
                listFibonacci.add(new)
            }
        }
        println("listFibonacci: $listFibonacci")
    }

    @Test
    fun generatePrimeNumber(){
        val integer = 100
        var count = 0
        val listOfPrime = mutableListOf<Int>()
        first@ for (i in 2..integer) {
            second@ for (j in 1..i) {
                if (i % j == 0) count +=1
                if (count > 2) break@second
            }
            if (count <= 2) listOfPrime.add(i)
            count = 0
        }
        println("generatePrimeNumber: $listOfPrime")
    }
}