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
import kotlinx.android.synthetic.main.activity_blocks.*


private var canCallConsole = true


private fun convertVarToNum(varName: String, varBlocksList: ArrayList<VarBlock>): String{
    varBlocksList.forEach { block ->
        if(block.name == varName){
            if(isVariableInString(block.value)){
                var nameCopy: String = block.value
                var pattern: Sequence<MatchResult> = Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = block.value)
                pattern = pattern.sortedBy { -it.value.length }
                pattern.forEach {
                    val number = convertVarToNum(it.value, varBlocksList)
                    nameCopy = nameCopy.replace(it.value, number)
                }
                val exception = ReversePolishNotation(nameCopy)
                Log.d("arrr", exception.RPN())
                return exception.RPN()
            }
            return block.value
        }
    }
    return "0"
}

private fun isVariableInString(s: String): Boolean {
    val pattern = Regex(pattern = """[^\d]""")
    return pattern.containsMatchIn(s)
}

private fun isDebugValid(varBlocksList: ArrayList<VarBlock>): String{
    varBlocksList.forEach { block ->
        when(block.blockType){
            "VAR"->{
                //name field
                val uncorrectVar = Regex(pattern = """^([^a-zA-Z])|[^\w\d_]|_$""".trimIndent())
                if(uncorrectVar.containsMatchIn(block.name)){
                    return "Uncorrect name of variable: ${block.name}"
                }

                //value field: variable check
                var variablePattern = Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = block.value)
                variablePattern.forEach {
                    var flag = false
                    varBlocksList.forEach { bl ->
                        if(bl.name == it.value){
                            flag = true
                        }
                    }
                    if(!flag){
                        return "Uncorrect name of value: ${block.value}"
                    }
                }

                //value field: signs check
                val uncorrectSigns = Regex(pattern = """[-+*\/%][-+*\/%]""".trimIndent())
            }

            "PRINT"->{

            }

            "IF"->{

            }
        }
    }
    return "Correct"
}

@SuppressLint("SetTextI18n")
private fun run(varBlocksList: ArrayList<VarBlock>, console: ConsolePageBinding){

    console.consoleOutput.text = ""
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
            val exception = ReversePolishNotation(nameCopy)
            console.consoleOutput.text = console.consoleOutput.text.toString() + exception.RPN() + "\n"
        }
    }
}


class BlocksActivity : Activity() {
    private var clicked = false
    lateinit var binding: ActivityBlocksBinding
    private val adapter = VarBlockAdapter()

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlocksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init(this)

        binding.addButton.setOnClickListener {
            val rotateOpen: Animation by lazy {
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.rotate_open_anim
                )
            }
            val rotateClose: Animation by lazy {
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.rotate_close_anim
                )
            }
            val fromBottom: Animation by lazy {
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.from_bottom_anim
                )
            }
            val toBottom: Animation by lazy {
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.to_bottom_anim
                )
            }
            if (!clicked) {
                binding.addButton.startAnimation(rotateOpen)
                binding.menu.visibility = View.VISIBLE
                binding.btnVariables.startAnimation(fromBottom)
                binding.btnIfElse.startAnimation(fromBottom)
                binding.btnPrint.startAnimation(fromBottom)

                binding.textView2.startAnimation(fromBottom)
                binding.textView4.startAnimation(fromBottom)
                binding.textView5.startAnimation(fromBottom)
                clicked = true
            } else {
                binding.addButton.startAnimation(rotateClose)

                clicked = false
                binding.btnVariables.startAnimation(toBottom)
                binding.btnIfElse.startAnimation(toBottom)
                binding.btnPrint.startAnimation(toBottom)

                binding.textView2.startAnimation(toBottom)
                binding.textView4.startAnimation(toBottom)
                binding.textView5.startAnimation(toBottom)
            }
        }


        val view = LayoutInflater.from(this).inflate(R.layout.console_page, null)

        binding.btnDebug.setOnClickListener {
            var l = adapter.callVarBlocksList()
            val console = ConsolePageBinding.bind(view)
            run(l, console)
        }

        binding.consoleButton.setOnClickListener {
            if (canCallConsole) {
                binding.layoutConsole.addView(view)
                canCallConsole = false
            } else {
                binding.layoutConsole.removeAllViews()
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

            val swipeHandler = object: SwipeToDelete(context) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val swipeAdapter = rcView.adapter as VarBlockAdapter //????
                    swipeAdapter.removeAt(viewHolder.adapterPosition)
                }
            }
            val itemTouchHelperSwipe = ItemTouchHelper(swipeHandler)
            itemTouchHelperSwipe.attachToRecyclerView(rcView) //???

            binding.btnVariables.setOnClickListener{
                /*val blockView = LayoutInflater.from(context).inflate(R.layout.variables_block, null)
                val layoutBlock = VariablesBlockBinding.bind(blockView)*/
                val block = VarBlock("", "", "VAR")
                adapter.addVarBlock(block)

            }

            binding.btnPrint.setOnClickListener{
                val block = VarBlock("", "", "PRINT")
                adapter.addVarBlock(block)
            }

            binding.btnIfElse.setOnClickListener{
                val block = VarBlock("", "", "IF")
                adapter.addVarBlock(block)
            }
        }
    }
}


