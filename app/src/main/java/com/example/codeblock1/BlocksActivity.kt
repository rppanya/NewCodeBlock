package com.example.codeblock1

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeblock1.databinding.CodePageBinding
import kotlinx.android.synthetic.main.code_page.*
import kotlinx.android.synthetic.main.console_page.view.*
import kotlinx.android.synthetic.main.variables_block.*


private fun onTouch(view: View, event: MotionEvent): Boolean {
    var dX:Float = 0f
    var dY:Float = 0f
    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            dX = view.x - event.rawX
            dY = view.y - event.rawY
        }
        MotionEvent.ACTION_MOVE -> view.animate()
            .x(event.rawX)
            .y(event.rawY)
            .setDuration(0)
            .start()
        MotionEvent.ACTION_UP -> {
            view.animate()
                .x(event.rawX)
                .y(event.rawY)
                .setDuration(0)
                .start()
        }
        else -> return false
    }
    return true
}

class BlocksActivity : Activity() {
    private var clicked = false
    lateinit var binding: CodePageBinding
    private val adapter = VarBlockAdapter()

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CodePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

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

        btnOpenConsole.setOnClickListener{

        }

    }

    @SuppressLint("InflateParams")
    private fun init() {
            binding.apply{
                rcView.layoutManager = LinearLayoutManager(this@BlocksActivity)
                rcView.adapter = adapter
                btnVariables.setOnClickListener{
                    val block = VarBlock("VAR")
                    adapter.addVarBlock(block)
                }
        }

        testButton.setOnClickListener{
            val view = LayoutInflater.from(this).inflate(R.layout.console_page, null)
            layout_console.addView(view)
            /* val test = ReversePolishNotation("(2+9*8")
            testButton.text = test.RPN()*/
        }
    }
}


