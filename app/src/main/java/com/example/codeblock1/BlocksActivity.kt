package com.example.codeblock1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Xml
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.code_page.*
import kotlinx.android.synthetic.main.variables_block.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast


class BlocksActivity : Activity() {
    private var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.code_page)

        addButton.setOnClickListener{
            val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
            val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
            val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
            val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
            if (!clicked) {
                addButton.startAnimation(rotateOpen)
                menu.visibility = View.VISIBLE
                btnVariables.startAnimation(fromBottom)
                btnIfElse.startAnimation(fromBottom)
                btnOpenConsole.startAnimation(fromBottom)

                textView2.startAnimation(fromBottom)
                textView4.startAnimation(fromBottom)
                textView5.startAnimation(fromBottom)
                clicked = true
            } else {
                addButton.startAnimation(rotateClose)

                clicked = false
                btnVariables.startAnimation(toBottom)
                btnIfElse.startAnimation(toBottom)
                btnOpenConsole.startAnimation(toBottom)

                textView2.startAnimation(toBottom)
                textView4.startAnimation(toBottom)
                textView5.startAnimation(toBottom)
            }
        }
        btnVariables.setOnClickListener{
            val view = layoutInflater.inflate(R.layout.variables_block, null, false)
            llMain.addView(view)
        }

        btnOpenConsole.setOnClickListener{

        }

    }
}


