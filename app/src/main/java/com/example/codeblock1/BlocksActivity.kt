package com.example.codeblock1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codeblock1.databinding.*
import kotlinx.android.synthetic.main.code_page.*


private var canCallConsole = true


private fun convertVarToNum(varName: String, varBlocksList: ArrayList<VarBlock>): String{
    varBlocksList.forEach {
        if(it.name == varName){
            return it.value
        }
    }
    return "0"
}

private fun isVariableInString(s: String): Boolean {
    val pattern = Regex(pattern = """[^\d]""")
    return pattern.containsMatchIn(s)
}

@SuppressLint("SetTextI18n")
private fun debug(varBlocksList: ArrayList<VarBlock>, console: ConsolePageBinding){
    varBlocksList.forEach { block ->
        if(block.blockType == "PRINT"){
            var nameCopy: String = block.name
            if(isVariableInString(block.name)){
                var pattern: Sequence<MatchResult> = Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = block.name)
                pattern = pattern.sortedBy { -it.value.length }
                pattern.forEach {
                    val number = convertVarToNum(it.value, varBlocksList)
                    nameCopy = nameCopy.replace(it.value, number)
                }
            }
            Log.d("arrr", block.name)
            Log.d("arrr", nameCopy)
            val exception = ReversePolishNotation(nameCopy)
            console.consoleOutput.text = exception.RPN()
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

        init(this)

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
                canCallConsole = true
            }
        }
    }

    private fun init(context: Context) {
        binding.apply{
            rcView.layoutManager = LinearLayoutManager(this@BlocksActivity)
            rcView.adapter = adapter
            val itemTouchHelper = ItemTouchHelper(adapter.simpleCellback)
            itemTouchHelper.attachToRecyclerView(rcView)

            btnVariables.setOnClickListener{
                /*val blockView = LayoutInflater.from(context).inflate(R.layout.variables_block, null)
                val layoutBlock = VariablesBlockBinding.bind(blockView)*/
                val block = VarBlock("NAME", "VALUE", "VAR")
                adapter.addVarBlock(block)
            }

            btnPrint.setOnClickListener{
                val block = VarBlock("NAME", "VALUE", "PRINT")
                adapter.addVarBlock(block)
            }

            btnIfElse.setOnClickListener{
                val block = VarBlock("NAME", "VALUE", "IF")
                adapter.addVarBlock(block)
            }
        }
    }
}


