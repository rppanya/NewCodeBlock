package com.example.codeblock1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.codeblock1.R.style.CustomAlertDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var clicked = false
    private var counter = 0
    private var variables = mutableListOf<Button>()
    //var container = findViewById<LinearLayout>(R.id.buttonsContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnVariables.setOnClickListener{
            createAlertForVariables("variables")
        }
        btnOperators.setOnClickListener{
            createAlertForVariables("operators")
        }
    }

    fun addClick(view: View) {
        val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
        val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
        val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
        val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
        if (!clicked) {
            addButton.startAnimation(rotateOpen)
            menu.visibility = View.VISIBLE
            btnVariables.startAnimation(fromBottom)
            btnOperators.startAnimation(fromBottom)
            floatingActionButton10.startAnimation(fromBottom)
            floatingActionButton11.startAnimation(fromBottom)

            textView2.startAnimation(fromBottom)
            textView3.startAnimation(fromBottom)
            textView4.startAnimation(fromBottom)
            textView5.startAnimation(fromBottom)
            clicked = true
        } else {
            addButton.startAnimation(rotateClose)

            clicked = false
            btnVariables.startAnimation(toBottom)
            btnOperators.startAnimation(toBottom)
            floatingActionButton10.startAnimation(toBottom)
            floatingActionButton11.startAnimation(toBottom)

            textView2.startAnimation(toBottom)
            textView3.startAnimation(toBottom)
            textView4.startAnimation(toBottom)
            textView5.startAnimation(toBottom)
        }
    }

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun createAlertForVariables(condition: String) {
        val builder = AlertDialog.Builder(this, CustomAlertDialog).create()

        if (condition == "variables") {
            val view = layoutInflater.inflate(R.layout.custom_view_layout, null)
            builder.setTitle("New variable")
            builder.setView(view)
            val wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT
            val lParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                wrapContent, wrapContent
            )
            view.findViewById<Button>(R.id.dialogDismiss_button).setOnClickListener {
                val btnNew = Button(this)
                val txt = view.findViewById<EditText>(R.id.editTextTextPersonName)
                btnNew.text = txt.text.toString()
                llMain.addView(btnNew, lParams)
                builder.dismiss()
                variables.add(btnNew)
                btnNew.setOnTouchListener(View.OnTouchListener{ _, motionEvent ->
                    onTouch(btnNew, motionEvent)
                })
            }
            view.findViewById<Button>(R.id.dialogDelete_button).setOnClickListener {
                builder.cancel()
            }
            builder.setCancelable(false)
            builder.show()

        } else if (condition == "operators") {

            val view = layoutInflater.inflate(R.layout.alert_for_operators, null)
            builder.setTitle("New operator")
            builder.setView(view)
            val wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT
            val lParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                wrapContent, wrapContent
            )
            view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                builder.cancel()
            }

            view.findViewById<Button>(R.id.buttonPlus).setOnClickListener {//пока только для + потому что писать для каждой кнопки так это странно
                                                                           // и надо подумать как сделать это для всех кнопок
                val btnNew = Button(this)
                btnNew.text = "+"
                llMain.addView(btnNew, lParams)
                builder.dismiss()
                variables.add(btnNew)
                btnNew.setOnTouchListener(View.OnTouchListener{ _, motionEvent ->
                    onTouch(btnNew, motionEvent)
                })

            }
            builder.setCancelable(false)
            builder.show()


        }
    }

    private fun onTouch(view: Button, event: MotionEvent): Boolean {
        var dX:Float = 0f
        var dY:Float = 0f
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> view.animate()
                .x(event.rawX + dX - 100)
                .y((((event.rawY + dY - 100)/150).toInt() * 150).toFloat())
                .setDuration(0)
                .start()
            MotionEvent.ACTION_UP -> {
                view.animate()
                    .x(25F)
                    .y((((event.rawY + dY - 100)/150).toInt() * 150).toFloat())
                    .setDuration(0)
                    .start()
            }
            else -> return false
        }
        return true
    }

}


class ReversePolishNotation(var dataString: String) {

    private fun processOperator(st: ArrayList<Int>, op: Char) {
        val r: Int = st.removeLast()
        val l: Int = st.removeLast()
        when (op) {
            '+' -> st.add(l + r)
            '-' -> st.add(l - r)
            '*' -> st.add(l * r)
            '/' -> st.add(l / r)
            '%' -> st.add(l % r)
        }
    }
    private fun isOperator(c: Char): Boolean { // возвращяем тру если один из символов ниже
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%'
    }

    private fun priority(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/', '%' -> 2
            else -> -1
        }
    }

    private fun RPN(): Int {
        val stackNumbers: ArrayList<Int> = ArrayList()
        val stackOperators: ArrayList<Char> = ArrayList()
        var i = 0
        while (i < dataString.length) {
            val current: Char = dataString[i]
            when {
                current == '(' -> stackOperators.add('(')
                current == ')' -> {
                    while (stackOperators.last() != '(') {
                        processOperator(stackNumbers, stackOperators.removeLast())
                    }
                    stackOperators.removeLast()
                }
                isOperator(current) -> {
                    while (stackOperators.isNotEmpty() && priority(stackOperators.last()) >= priority(current)) {
                        processOperator(stackNumbers, stackOperators.removeLast())
                    }
                    stackOperators.add(current)
                }
                else -> {
                    var operand = ""
                    while (i < dataString.length && Character.isDigit(dataString[i])) {
                        operand += dataString[i++]
                    }
                    --i
                    stackNumbers.add(Integer.parseInt(operand))
                }
            }
        }
        while (stackOperators.isNotEmpty()) {
            processOperator(stackNumbers, stackOperators.removeLast())
        }
        return stackNumbers[0]
    }
}

