package academy.learnprogramming.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val TEXT_OPERATION = "TextOperation"
private const val DOUBLE_OPERAND1 = "DoubleOperand1"
private const val BOOLEAN_OPERAND1_INITIALIZED = "BooleanOperand1Initialized"

class MainActivity : AppCompatActivity() {
    // Variables to hold the operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)

        buttonNegate.setOnClickListener {
            if(newNumber.text.startsWith("-")) {
                newNumber.setText(newNumber.text.substring(1))
            } else {
                val updatedNumber:String = "-" + newNumber.text.toString()
                newNumber.setText(updatedNumber)
            }
        }
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) { // Initial state, first number entered
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (operand1 == 0.0) {
                    Double.NaN // handle attempt to divide by zero
                } else {
                    operand1!! / value // !! is needed because operand1 is a nullable Double, operand2 is a Double
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_OPERATION, pendingOperation)
        if (operand1 != null) {
            outState.putDouble(DOUBLE_OPERAND1, operand1!!)
            outState.putBoolean(BOOLEAN_OPERAND1_INITIALIZED, true)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedPendingOperation = savedInstanceState.getString(TEXT_OPERATION, "=")
        pendingOperation = savedPendingOperation
        operation.text = savedPendingOperation
        val operand1Initialized = savedInstanceState.getBoolean(BOOLEAN_OPERAND1_INITIALIZED, false)
        operand1 = if (operand1Initialized) {
            savedInstanceState.getDouble(DOUBLE_OPERAND1)
        } else {
            null
        }
    }
}
