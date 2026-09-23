package com.tomasthrawat.materialcalculator

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class CalculatorEngine {
    private val mathContext = MathContext(16, RoundingMode.HALF_UP)

    fun evaluate(expression: String): String {
        val source = expression
            .replace('×', '*')
            .replace('÷', '/')
            .replace(" ", "")

        require(source.isNotEmpty()) { "Empty expression" }

        val numbers = ArrayDeque<BigDecimal>()
        val operators = ArrayDeque<Char>()
        var index = 0

        while (index < source.length) {
            val char = source[index]

            if (char.isDigit() || char == '.' || isUnarySign(source, index)) {
                val start = index
                if (source[index] == '+' || source[index] == '-') {
                    index++
                }

                var hasDigit = false
                var dots = 0
                while (index < source.length) {
                    val current = source[index]
                    when {
                        current.isDigit() -> {
                            hasDigit = true
                            index++
                        }
                        current == '.' -> {
                            dots++
                            require(dots <= 1) { "Invalid number" }
                            index++
                        }
                        else -> break
                    }
                }

                require(hasDigit) { "Invalid number" }
                numbers.addLast(source.substring(start, index).toBigDecimal())
                continue
            }

            require(char in "+-*/") { "Invalid operator" }

            while (operators.isNotEmpty() &&
                precedence(operators.last()) >= precedence(char)
            ) {
                reduceTop(numbers, operators.removeLast())
            }

            operators.addLast(char)
            index++
        }

        while (operators.isNotEmpty()) {
            reduceTop(numbers, operators.removeLast())
        }

        require(numbers.size == 1) { "Invalid expression" }
        return format(numbers.removeLast())
    }

    private fun isUnarySign(source: String, index: Int): Boolean {
        val char = source[index]
        if (char != '+' && char != '-') return false
        return index == 0 || source[index - 1] in "+-*/"
    }

    private fun precedence(operator: Char): Int = when (operator) {
        '+', '-' -> 1
        '*', '/' -> 2
        else -> 0
    }

    private fun reduceTop(numbers: ArrayDeque<BigDecimal>, operator: Char) {
        require(numbers.size >= 2) { "Invalid expression" }
        val right = numbers.removeLast()
        val left = numbers.removeLast()

        val result = when (operator) {
            '+' -> left.add(right, mathContext)
            '-' -> left.subtract(right, mathContext)
            '*' -> left.multiply(right, mathContext)
            '/' -> {
                require(right.compareTo(BigDecimal.ZERO) != 0) { "Division by zero" }
                left.divide(right, mathContext)
            }
            else -> error("Unsupported operator")
        }

        numbers.addLast(result)
    }

    private fun format(value: BigDecimal): String {
        if (value.compareTo(BigDecimal.ZERO) == 0) return "0"
        val normalized = value.stripTrailingZeros()
        return normalized.toPlainString()
    }
}
