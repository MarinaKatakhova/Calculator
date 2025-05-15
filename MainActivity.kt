import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var resultField: EditText
    private var currentInput = ""
    private var firstOperand = 0.0
    private var currentOperator = ""
    private var resetField = false
    private var lastResult = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultField = findViewById(R.id.resultField)
        resultField.setText("0")

        // Назначение обработчиков для цифровых кнопок
        setNumberButton(R.id.btn0, "0")
        setNumberButton(R.id.btn1, "1")
        setNumberButton(R.id.btn2, "2")
        setNumberButton(R.id.btn3, "3")
        setNumberButton(R.id.btn4, "4")
        setNumberButton(R.id.btn5, "5")
        setNumberButton(R.id.btn6, "6")
        setNumberButton(R.id.btn7, "7")
        setNumberButton(R.id.btn8, "8")
        setNumberButton(R.id.btn9, "9")

        // Кнопка точки
        findViewById<Button>(R.id.btnDot).setOnClickListener {
            if (resetField) {
                currentInput = "0."
                resetField = false
            } else if (!currentInput.contains(".")) {
                if (currentInput.isEmpty()) {
                    currentInput = "0."
                } else {
                    currentInput += "."
                }
            }
            resultField.setText(currentInput)
        }

        // Кнопка очистки
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            currentInput = ""
            firstOperand = 0.0
            currentOperator = ""
            lastResult = 0.0
            resultField.setText("0")
            resetField = false
        }

        // Кнопка удаления последнего символа
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                resultField.setText(if (currentInput.isEmpty()) "0" else currentInput)
            }
        }

        // Операторы
        setOperatorButton(R.id.btnAdd, "+")
        setOperatorButton(R.id.btnSubtract, "-")
        setOperatorButton(R.id.btnMultiply, "×")
        setOperatorButton(R.id.btnDivide, "÷")

        // Кнопка равно
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            calculateResult()
        }

        // Кнопка процента
        findViewById<Button>(R.id.btnPercent).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                val value = currentInput.toDouble() / 100
                currentInput = value.toString()
                resultField.setText(currentInput)
            }
        }

        // Кнопка изменения знака
        findViewById<Button>(R.id.btnSign).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                val value = currentInput.toDouble() * -1
                currentInput = value.toString()
                resultField.setText(currentInput)
            }
        }
    }

    private fun setNumberButton(buttonId: Int, number: String) {
        findViewById<Button>(buttonId).setOnClickListener {
            if (resetField) {
                currentInput = ""
                resetField = false
            }
            // Ограничение на длину числа
            if (currentInput.length < 15) {
                currentInput += number
                resultField.setText(currentInput)
            }
        }
    }

    private fun setOperatorButton(buttonId: Int, operator: String) {
        findViewById<Button>(buttonId).setOnClickListener {
            if (currentInput.isNotEmpty() || lastResult != 0.0) {
                if (currentInput.isEmpty() && lastResult != 0.0) {
                    firstOperand = lastResult
                } else {
                    firstOperand = currentInput.toDouble()
                }
                currentOperator = operator
                resetField = true
            }
        }
    }

    private fun calculateResult() {
        if (currentOperator.isNotEmpty()) {
            val secondOperand = if (currentInput.isNotEmpty()) {
                currentInput.toDouble()
            } else {
                lastResult
            }

            val result = when (currentOperator) {
                "+" -> firstOperand + secondOperand
                "-" -> firstOperand - secondOperand
                "×" -> firstOperand * secondOperand
                "÷" -> if (secondOperand != 0.0) firstOperand / secondOperand else Double.NaN
                else -> Double.NaN
            }

            lastResult = if (result.isNaN()) 0.0 else result
            currentInput = if (result.isNaN()) {
                "Error"
            } else {
                // Форматирование результата (удаление .0 для целых чисел)
                if (result % 1 == 0.0) {
                    result.toLong().toString()
                } else {
                    result.toString()
                }
            }
            resultField.setText(currentInput)
            resetField = true
            currentOperator = ""
        }
    }
}