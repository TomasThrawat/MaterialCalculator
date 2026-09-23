package com.tomasthrawat.materialcalculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CalculatorEngineTest {

    private val engine = CalculatorEngine()

    @Test
    fun respectsOperatorPrecedence() {
        assertEquals("14", engine.evaluate("2+3*4"))
    }

    @Test
    fun handlesDecimals() {
        assertEquals("2.5", engine.evaluate("10/4"))
    }

    @Test
    fun handlesUnaryMinus() {
        assertEquals("-3", engine.evaluate("-5+2"))
        assertEquals("-6", engine.evaluate("2*-3"))
    }

    @Test
    fun handlesUnicodeSubtractOperator() {
        assertEquals("0", engine.evaluate("8−8"))
        assertEquals("4", engine.evaluate("12−8"))
        assertEquals("-4", engine.evaluate("8−12"))
    }

    @Test
    fun rejectsDivisionByZero() {
        assertThrows(IllegalArgumentException::class.java) {
            engine.evaluate("10/0")
        }
    }
}
