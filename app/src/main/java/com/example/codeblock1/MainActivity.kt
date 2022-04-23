package com.example.codeblock1

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    private var clicked = false

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

    fun createAlertForVariables(view: View) {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.custom_view_layout, null)
        val enterButton = view.findViewById<Button>(R.id.dialogDismiss_button)
        val cancelButton = view.findViewById<Button>(R.id.dialogDelete_button)
        builder.setView(view)
        builder.setTitle("New variable")
        enterButton.setOnClickListener {
            builder.dismiss()
        }
        cancelButton.setOnClickListener {
            builder.cancel()
        }
        builder.setCancelable(false)
        builder.show()
    }
}


