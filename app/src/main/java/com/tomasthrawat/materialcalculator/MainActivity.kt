package com.tomasthrawat.materialcalculator

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import java.math.BigDecimal

class MainActivity : ComponentActivity() {

    private val engine = CalculatorEngine()
    private val expression = StringBuilder()
    private lateinit var expressionView: TextView
    private lateinit var resultView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expressionView = findViewById(R.id.expression)
        resultView = findViewById(R.id.result)

        bindButtons()
        updateDisplay()
    }

    private fun bindButtons() {
        val numberIds = intArrayOf(
            R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
            R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
            R.id.button_8, R.id.button_9
        )

        numberIds.forEach { id ->
            findViewById<MaterialButton>(id).setOnClickListener { view ->
                appendNumber((view as MaterialButton).text.toString())
            }
        }

        findViewById<MaterialButton>(R.id.button_decimal).setOnClickListener {
            appendDecimal()
        }

        mapOf(
            R.id.button_add to "+",
            R.id.button_subtract to "−",
            R.id.button_multiply to "×",
            R.id.button_divide to "÷"
        ).forEach { (id, operator) ->
            findViewById<MaterialButton>(id).setOnClickListener {
                appendOperator(operator)
            }
        }

        findViewById<MaterialButton>(R.id.button_clear).setOnClickListener {
            expression.clear()
            resultView.text = "0"
            updateDisplay()
        }

        findViewById<MaterialButton>(R.id.button_backspace).setOnClickListener {
            if (expression.isNotEmpty()) expression.deleteCharAt(expression.lastIndex)
            updateDisplay()
        }

        findViewById<MaterialButton>(R.id.button_percent).setOnClickListener {
            applyPercent()
        }

        findViewById<MaterialButton>(R.id.button_sign).setOnClickListener {
            toggleSign()
        }

        findViewById<MaterialButton>(R.id.button_equals).setOnClickListener {
            calculate()
        }
    }

    private fun appendNumber(number: String) {
        val last = expression.lastOrNull()
        if (last == '×' || last == '÷' || last == '+' || last == '−') {
            resultView.text = "0"
        }

        if (currentNumberIsZero()) {
            val start = currentNumberStart()
            if (start < expression.length) {
                expression.delete(start, expression.length)
            }
        }

        expression.append(number)
        updateDisplay()
    }

    private fun appendDecimal() {
        val start = currentNumberStart()
        val current = expression.substring(start)
        if (current.contains('.')) return

        if (expression.isEmpty() || expression.last() in "×÷+−") {
            expression.append("0.")
        } else {
            expression.append('.')
        }
        updateDisplay()
    }

    private fun appendOperator(operator: String) {
        if (expression.isEmpty()) return
        val last = expression.last()
        if (last in "×÷+−") {
            expression.setCharAt(expression.lastIndex, operator[0])
        } else if (last != '.') {
            expression.append(operator)
        }
        updateDisplay()
    }

    private fun applyPercent() {
        if (expression.isEmpty() || expression.last() in "×÷+−.") return

        val start = currentNumberStart()
        val token = expression.substring(start)
        val percent = try {
            BigDecimal(token).divide(BigDecimal(100))
        } catch (_: NumberFormatException) {
            return
        }

        expression.replace(start, expression.length, percent.stripTrailingZeros().toPlainString())
        updateDisplay()
    }

    private fun toggleSign() {
        if (expression.isEmpty()) {
            expression.append('-')
            updateDisplay()
            return
        }

        if (expression.last() in "×÷+−") {
            expression.append('-')
            updateDisplay()
            return
        }

        val start = currentNumberStart()
        if (start >= expression.length) return

        val token = expression.substring(start)
        val replacement = if (token.startsWith('-')) token.removePrefix("-") else "-$token"
        expression.replace(start, expression.length, replacement)
        updateDisplay()
    }

    private fun calculate() {
        if (expression.isEmpty() || expression.last() in "×÷+−.") return

        try {
            val value = engine.evaluate(expression.toString())
            resultView.text = value
        } catch (_: IllegalArgumentException) {
            resultView.text = getString(R.string.error)
        }
    }

    private fun currentNumberStart(): Int {
        var index = expression.length - 1
        while (index >= 0 && expression[index] !in "×÷+−") {
            index--
        }
        return index + 1
    }

    private fun currentNumberIsZero(): Boolean {
        if (expression.isEmpty()) return false
        val start = currentNumberStart()
        val token = expression.substring(start)
        return token == "0"
    }

    private fun updateDisplay() {
        expressionView.text = expression.toString().ifEmpty { "0" }
        expressionView.contentDescription = expressionView.text
        resultView.contentDescription = resultView.text
    }
}
