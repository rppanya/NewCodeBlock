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
import androidx.core.view.marginBottom
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    private var clicked = false

    private var counter = 0
    private var variables = mutableListOf<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun addClick(view: View) {
        if (!clicked) {
            addButton.startAnimation(rotateOpen)
            menu.visibility = View.VISIBLE
            floatingActionButton8.startAnimation(fromBottom)
            floatingActionButton9.startAnimation(fromBottom)
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
            floatingActionButton8.startAnimation(toBottom)
            floatingActionButton9.startAnimation(toBottom)
            floatingActionButton10.startAnimation(toBottom)
            floatingActionButton11.startAnimation(toBottom)

            textView2.startAnimation(toBottom)
            textView3.startAnimation(toBottom)
            textView4.startAnimation(toBottom)
            textView5.startAnimation(toBottom)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun createAlertForVariables(view: View) {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.custom_view_layout, null)
        val enterButton = view.findViewById<Button>(R.id.dialogDismiss_button)
        val cancelButton = view.findViewById<Button>(R.id.dialogDelete_button)
        builder.setView(view)
        builder.setTitle("New variable")
        val wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT
        val lParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            wrapContent, wrapContent
        )
        enterButton.setOnClickListener {
            val btnNew = Button(this)
            val txt = view.findViewById<EditText>(R.id.editTextTextPersonName)
            btnNew.text = txt.text.toString()
            llMain.addView(btnNew, lParams)
            builder.dismiss()
            variables.add(btnNew)
            btnNew.setOnTouchListener(View.OnTouchListener{ view, motionEvent ->
                onTouch(btnNew, motionEvent)
            })
        }
        cancelButton.setOnClickListener {
            builder.cancel()
        }
        builder.setCancelable(false)
        builder.show()

    }

    fun onTouch(view: Button, event: MotionEvent): Boolean {
        var dX:Float = 0f
        var dY:Float = 0f
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> view.animate()
                .x(event.rawX + dX - 130)
                .y(event.rawY + dY - 250)
                .setDuration(0)
                .start()
            else -> return false
        }
        return true
    }
}



