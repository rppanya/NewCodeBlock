package com.example.codeblock1

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeblock1.databinding.CodePageBinding
import com.example.codeblock1.databinding.ConsolePageBinding
import com.example.codeblock1.databinding.VariablesBlockBinding
import kotlinx.android.synthetic.main.code_page.*


private var canCallConsole = true

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

private fun setValuesToBlocks(){

}

private fun debug(varBlocksList: ArrayList<VarBlock>, console: ConsolePageBinding){
    varBlocksList.forEach{
        if(it.blockType == "PRINT"){
            Log.d("arrr", it.layoutBlock.nameOfVariable.text.toString())
            console.consoleOutput.text = it.layoutBlock.nameOfVariable.text.toString()
        }
    }
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
        val blockView = LayoutInflater.from(this).inflate(R.layout.variables_block, null)
        val layoutBlock = VariablesBlockBinding.bind(blockView)

        layoutBlock.nameOfVariable.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                layoutBlock.nameOfVariable.setText(layoutBlock.nameOfVariable.text.toString() + s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        init(layoutBlock)

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
                btnPrint.startAnimation(fromBottom)

                textView2.startAnimation(fromBottom)
                textView4.startAnimation(fromBottom)
                textView5.startAnimation(fromBottom)
                clicked = true
            } else {
                addButton.startAnimation(rotateClose)

                clicked = false
                btnVariables.startAnimation(toBottom)
                btnIfElse.startAnimation(toBottom)
                btnPrint.startAnimation(toBottom)

                textView2.startAnimation(toBottom)
                textView4.startAnimation(toBottom)
                textView5.startAnimation(toBottom)
            }
        }


        val view = LayoutInflater.from(this).inflate(R.layout.console_page, null)

        btnDebug.setOnClickListener{
            var l = adapter.callVarBlocksList()
            val console = ConsolePageBinding.bind(view)
            debug(l, console)
        }

        consoleButton.setOnClickListener{
            if(canCallConsole) {
                layout_console.addView(view)
                canCallConsole = false
               /* val test = ReversePolishNotation("1+3+4*2")
                console.consoleOutput.text = test.RPN()*/
            }
            else {
                layout_console.removeAllViews()
                consoleButton.text = "CHLEN"
                canCallConsole = true
            }
        }
    }


    @SuppressLint("InflateParams")
    private fun init(layoutBlock: VariablesBlockBinding) {
            binding.apply{
                rcView.layoutManager = LinearLayoutManager(this@BlocksActivity)
                rcView.adapter = adapter

                btnVariables.setOnClickListener{
                    val block = VarBlock("NAME", "VALUE", "VAR", layoutBlock)
                    adapter.addVarBlock(block)
                }

                btnPrint.setOnClickListener{
                    val block = VarBlock("NAME", "VALUE", "PRINT", layoutBlock)
                    adapter.addVarBlock(block)
                }

                btnIfElse.setOnClickListener{
                    val block = VarBlock("NAME", "VALUE", "IF", layoutBlock)
                    adapter.addVarBlock(block)
                }
        }
    }
}


