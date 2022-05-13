package com.example.codeblock1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codeblock1.databinding.ActivityBlocksBinding
import com.example.codeblock1.databinding.ConsolePageBinding


private var canCallConsole = true


private fun convertVarToNum(varName: String, variables: ArrayList<VarValue>): String{
    variables.forEach { block ->
        if(block.name == varName){
            if(isVariableInString(block.value)){
                var nameCopy: String = block.value
                var pattern: Sequence<MatchResult> = Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = block.value)
                pattern = pattern.sortedBy { -it.value.length }
                pattern.forEach {
                    val number = convertVarToNum(it.value, variables)
                    nameCopy = nameCopy.replace(it.value, number)
                }
                val exception = ReversePolishNotation(nameCopy)
                Log.d("arrr", exception.RPN())
                return exception.RPN()
            }
            return block.value
        }
    }
    return "ERROR"
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
                val incorrectVar = Regex(pattern = """^([^a-zA-Z])|[^\w\d_]|_$""".trimIndent())
                if(incorrectVar.containsMatchIn(block.name)){
                    return "Incorrect name of variable: ${block.name}"
                }

                //value field: variable check
                val variablePattern = Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = block.value)
                variablePattern.forEach {
                    var flag = false
                    varBlocksList.forEach { bl ->
                        if(bl.name == it.value){
                            flag = true
                        }
                    }
                    if(!flag){
                        return "Incorrect name of value: ${block.value}"
                    }
                }

                //value field: signs check
                val incorrectSigns = Regex(pattern = """[-+*/%][-+*/%]""".trimIndent())
                if(incorrectSigns.containsMatchIn(block.value)){
                    return "Incorrect arithmetic expression: ${block.value}"
                }
            }

            "PRINT"->{
                val variablePattern = Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = block.name)
                variablePattern.forEach { variable ->
                    var varInd: Int = -1
                    for(i in 0 until varBlocksList.size){
                        if(varBlocksList[i].name == variable.value && varBlocksList[i].blockType == "VAR"){
                            varInd = i
                            break
                        }
                    }
                    if(varInd > varBlocksList.indexOf(block) || varInd == -1){
                        return "Can't find variable: ${variable.value}"
                    }
                }
            }

            "IF"->{

            }
        }
    }
    return "Correct"
}

private fun findVarInd(nameOfVar: String, variables: ArrayList<VarValue>): Int{
    for(i in 0 until variables.size){
        if(variables[i].name == nameOfVar){
            return i
        }
    }
    return -1
}

@SuppressLint("SetTextI18n")
private fun run(varBlocksList: ArrayList<VarBlock>, console: ConsolePageBinding){
    var correctness = isDebugValid(varBlocksList)
    var variables = ArrayList<VarValue>()
    if(correctness != "Correct"){
        console.consoleOutput.text = correctness
        return
    }

    console.consoleOutput.text = ""
    var i: Int = 0
    while(i < varBlocksList.size) {
        when (varBlocksList[i].blockType) {
            "VAR" ->{
                val ind = findVarInd(varBlocksList[i].name, variables)
                if(ind != -1){
                    var flag = false
                    var varPattern: Sequence<MatchResult> =
                        Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = varBlocksList[i].value)
                    varPattern.forEach {
                        if(it.value == variables[ind].name){
                            val number = convertVarToNum(it.value, variables)
                            variables[ind].value = varBlocksList[i].value
                            variables[ind].value = variables[ind].value.replace(it.value, number)
                            flag = true
                        }
                    }
                    if(!flag){
                        variables[ind].value = varBlocksList[i].value
                    }
                } else {
                    variables.add(VarValue(varBlocksList[i].name, varBlocksList[i].value))
                }
            }

            "PRINT" -> {
                var nameCopy: String = varBlocksList[i].name
                if (isVariableInString(varBlocksList[i].name)) {
                    var pattern: Sequence<MatchResult> =
                        Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = varBlocksList[i].name)
                    pattern = pattern.sortedBy { -it.value.length }
                    pattern.forEach {
                        val number = convertVarToNum(it.value, variables)
                        nameCopy = nameCopy.replace(it.value, number)
                    }
                }
                try {
                    val exception = ReversePolishNotation(nameCopy)
                    console.consoleOutput.text =
                        console.consoleOutput.text.toString() + exception.RPN() + "\n"
                } catch (e: Exception) {
                    console.consoleOutput.text = "Incorrect PRINT value"
                }
            }

            "IF" -> {
                var nameCopy: String = varBlocksList[i].name
                if (isVariableInString(varBlocksList[i].name)) {
                    var pattern: Sequence<MatchResult> =
                        Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = varBlocksList[i].name)
                    pattern = pattern.sortedBy { -it.value.length }
                    pattern.forEach {
                        val number = convertVarToNum(it.value, variables)
                        nameCopy = nameCopy.replace(it.value, number)
                    }
                }
                val condition = InterpreterForInequalities(nameCopy)
                if(!condition.interpretInequality()) {
                    var skip = 0
                    for(j in i + 1 until varBlocksList.size){
                        if(varBlocksList[j].blockType != "END_IF"){
                            skip++
                        }
                        else{
                            break
                        }
                    }
                   i += (skip+1)
                }
            }
        }
        i++
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
                /*binding.btnVariables.startAnimation(toBottom)
                binding.btnIfElse.startAnimation(toBottom)
                binding.btnPrint.startAnimation(toBottom)

                binding.textView2.startAnimation(toBottom)
                binding.textView4.startAnimation(toBottom)
                binding.textView5.startAnimation(toBottom)*/


                binding.menu.visibility = View.GONE
            }
        }


        val view = LayoutInflater.from(this).inflate(R.layout.console_page, null)

        binding.btnDebug.setOnClickListener {
            val varBlocksList = adapter.callVarBlocksList()
            val console = ConsolePageBinding.bind(view)
            //val variables = adapter.callVariablesList()
            run(varBlocksList, console)
        }

        binding.consoleButton.setOnClickListener {
            if (canCallConsole) {
                binding.layoutConsole.addView(view)
                canCallConsole = false
                binding.addButton.hide()

            } else {
                binding.layoutConsole.removeAllViews()
                canCallConsole = true
                binding.addButton.show()
            }

        }
        binding.backButton.setOnClickListener{
            val current = Intent(this@BlocksActivity, MainActivity::class.java)
            startActivity(current)
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
                    val swipeAdapter = rcView.adapter as VarBlockAdapter
                    swipeAdapter.removeBlock(viewHolder.adapterPosition)
                }
            }
            val itemTouchHelperSwipe = ItemTouchHelper(swipeHandler)
            itemTouchHelperSwipe.attachToRecyclerView(rcView)

            binding.btnVariables.setOnClickListener{
                val block = VarBlock("", "", "VAR")
                adapter.addVarBlock(block)

            }

            binding.btnPrint.setOnClickListener{
                val block = VarBlock("", "", "PRINT")
                adapter.addVarBlock(block)
            }

            binding.btnIfElse.setOnClickListener{
                var block = VarBlock("", "", "IF")
                adapter.addVarBlock(block)
                block = VarBlock("", "", "END_IF")
                adapter.addVarBlock(block)
            }
        }
    }
}


