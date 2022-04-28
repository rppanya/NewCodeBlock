package com.example.codeblock1

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.code_page.*


class BlocksActivity : Activity() {
    private var clicked = true
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
                floatingActionButton10.startAnimation(fromBottom)

                textView2.startAnimation(fromBottom)
                textView4.startAnimation(fromBottom)
                clicked = true
            } else {
                addButton.startAnimation(rotateClose)

                clicked = false
                btnVariables.startAnimation(toBottom)
                floatingActionButton10.startAnimation(toBottom)

                textView2.startAnimation(toBottom)
                textView4.startAnimation(toBottom)
            }
        }
        btnVariables.setOnClickListener{

        }

    }
}