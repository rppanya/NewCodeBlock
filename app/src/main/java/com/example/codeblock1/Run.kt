package com.example.codeblock1

import android.annotation.SuppressLint
import com.example.codeblock1.databinding.ConsolePageBinding

class Run {

    private val variables = ArrayList<VarValue>()
    private val stackForIf = ArrayList<Int>()
    private val stackForWhile = ArrayList<Int>()


    private fun variablesValidness(varStr: String, varBlocksList: ArrayList<VarBlock>, index: Int): String{
        val variablePattern = Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = varStr)
        variablePattern.forEach { variable ->
            var varInd: Int = -1
            for(i in 0 until varBlocksList.size){
                if(varBlocksList[i].name == variable.value && varBlocksList[i].blockType == "VAR"){
                    varInd = i
                    break
                }
            }
            if(varInd > index || varInd == -1){
                return variable.value
            }
        }
        return "-1"
    }

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
                    //Log.d("arrr", exception.RPN())
                    return exception.RPN()
                }
                return block.value
            }
        }
        return "ERROR"
    }

    private fun isVariableInString(s: String): Boolean {
        val pattern = Regex(pattern = """[^\d<>(==)(!=)(<=)(>=)]""")
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
                    val incorrect = variablesValidness(block.value, varBlocksList, varBlocksList.indexOf(block))
                    if(incorrect != "-1"){
                        return "Can't find variable: $incorrect in VAR"
                    }

                    //value field: signs check
                    val incorrectSigns = Regex(pattern = """[-+*/%][-+*/%]+""".trimIndent())
                    if(incorrectSigns.containsMatchIn(block.value)){
                        return "Incorrect arithmetic expression: ${block.value}"
                    }
                }

                "PRINT"->{
                    val incorrect = variablesValidness(block.name, varBlocksList, varBlocksList.indexOf(block))
                    if(incorrect != "-1"){
                        return "Can't find variable $incorrect in PRINT"
                    }
                }


                "IF"->{
                    val ifPattern = Regex("""((((([a-z]|[A-Z])[\w\d_]*)|(\d+)))([+\-/*%](((([a-z]|[A-Z])[\w\d_]*)|(\d+))))*)(>|<|>=|<=|==|!=)((((([a-z]|[A-Z])[\w\d_]*)|(\d+)))([+\-\/*%](((([a-z]|[A-Z])[\w\d_]*)|(\d+))))*)""".trimIndent()).findAll(input = block.name)
                    var flag = false
                    ifPattern.forEach {
                        flag = true
                        if(it.value.length != block.name.length){
                            return "Incorrect IF expression"
                        }
                    }
                    if(!flag){
                        return "Incorrect IF expression"
                    }

                    val incorrect = variablesValidness(block.name, varBlocksList, varBlocksList.indexOf(block))
                    if(incorrect != "-1"){
                        return "Can't find variable $incorrect in IF"
                    }
                }
                "WHILE"->{
                    val ifPattern = Regex("""((((([a-z]|[A-Z])[\w\d_]*)|(\d+)))([+\-/*%](((([a-z]|[A-Z])[\w\d_]*)|(\d+))))*)(>|<|>=|<=|==|!=)((((([a-z]|[A-Z])[\w\d_]*)|(\d+)))([+\-\/*%](((([a-z]|[A-Z])[\w\d_]*)|(\d+))))*)""".trimIndent()).findAll(input = block.name)
                    var flag = false

                    if(!isVariableInString(block.name)) {
                        return "Can't find variables in WHILE"
                    }

                    ifPattern.forEach {
                        flag = true
                        if(it.value.length != block.name.length){
                            return "Incorrect WHILE expression"
                        }
                    }
                    if(!flag){
                        return "Incorrect WHILE expression"
                    }

                    val incorrect = variablesValidness(block.name, varBlocksList, varBlocksList.indexOf(block))
                    if(incorrect != "-1"){
                        return "Can't find variable $incorrect in WHILE"
                    }

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

    private fun varBeforeIf(VarBlocksList: ArrayList<VarBlock>, start: Int, finish: Int, name: String) : Boolean {
        for (i in finish downTo start) {
            if (VarBlocksList[i].blockType == "VAR" && VarBlocksList[i].name == name) {
                return true
            }
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    fun run(varBlocksList: ArrayList<VarBlock>, console: ConsolePageBinding){

        var counterWhile = 0
        stackForIf.clear()
        variables.clear()
        val correctness = isDebugValid(varBlocksList)

        if(correctness != "Correct"){
            console.consoleOutput.text = correctness
            return
        }

        console.consoleOutput.text = ""
        var i = 0
        while(i < varBlocksList.size) {
            when (varBlocksList[i].blockType) {
                "VAR" -> {
                    val nameCopy: String = varBlocksList[i].name
                    val valueCopy: String = varBlocksList[i].value
                    var indexIfVarNotAvailable = 0

                    if (nameCopy != "") {
                        indexIfVarNotAvailable = findVarInd(nameCopy, variables)

                        if (indexIfVarNotAvailable != -1) {
                            var flag = false
                            val varPattern: Sequence<MatchResult> =
                                Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = varBlocksList[i].value)
                            varPattern.forEach {
                                if(it.value == variables[indexIfVarNotAvailable].name){
                                    val number = convertVarToNum(it.value, variables)
                                    variables[indexIfVarNotAvailable].value = varBlocksList[i].value
                                    variables[indexIfVarNotAvailable].value = variables[indexIfVarNotAvailable].value.replace(it.value, number)
                                    flag = true
                                }
                            }
                            if(!flag){
                                variables[indexIfVarNotAvailable].value = varBlocksList[i].value
                            }

                        } else {
                            variables.add(VarValue(nameCopy, valueCopy))
                        }
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
                    stackForIf.add(i)

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
                    if (!condition.interpretInequality()) {
                        var skip = 0
                        for (j in i + 1 until varBlocksList.size) {
                            if (varBlocksList[j].blockType != "END_IF") {
                                skip++
                            } else {
                                break
                            }
                        }
                        i += (skip + 1)
                    } else {
                        if (varBlocksList[i - 1].blockType == "ELSE") {
                            var skip = 0
                            for (j in i until varBlocksList.size) {
                                if (varBlocksList[j].blockType != "END_ELSE") {
                                    skip++
                                } else {
                                    break
                                }
                            }
                            i += (skip + 1)
                        }
                    }
                }
                "END_IF" -> {
                    if (stackForIf.isEmpty()) {
                        console.consoleOutput.text = "Invalid IF operator brackets"
                        return
                    } else {
                        for (j in i - 1 downTo stackForIf.last()) {
                            if (varBlocksList[j].blockType == "VAR") {
                                if (!varBeforeIf(varBlocksList, 0, stackForIf.last(), varBlocksList[j].name)) {

                                    for (k in 0 until variables.size) {
                                        if (variables[k].name == varBlocksList[j].name) {
                                            variables.removeAt(k)
                                        }
                                    }
                                }
                            }
                        }
                        stackForIf.removeAt(stackForIf.lastIndex)
                    }
                }
                "WHILE" -> {
                    counterWhile++
                    stackForWhile.add(i)
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
                    if (!condition.interpretInequality()) {
                        var skip = 0
                        for (j in i + 1 until varBlocksList.size) {
                            if (varBlocksList[j].blockType != "END_WHILE") {
                                skip++
                            } else {
                                break
                            }
                        }
                        i += (skip + 1)
                    }
                    if (counterWhile > 500) {
                        console.consoleOutput.text = "Invalid WHILE"
                        return
                    }
                }
                "END_WHILE" -> {
                    if (stackForWhile.isEmpty()) {
                        console.consoleOutput.text = "Invalid WHILE operator brackets"
                        return
                    } else {

                        var nameCopy: String = varBlocksList[stackForWhile.last()].name
                        if (isVariableInString(varBlocksList[stackForWhile.last()].name)) {
                            var pattern: Sequence<MatchResult> =
                                Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = varBlocksList[stackForWhile.last()].name)
                            pattern = pattern.sortedBy { -it.value.length }
                            pattern.forEach {
                                val number = convertVarToNum(it.value, variables)
                                nameCopy = nameCopy.replace(it.value, number)
                            }
                        }
                        try {
                            val condition = InterpreterForInequalities(nameCopy)
                            if (condition.interpretInequality()) {
                                i = stackForWhile.last() - 1
                            } else {
                                stackForWhile.removeAt(stackForWhile.lastIndex)
                            }
                        } catch (e: Exception) {
                            console.consoleOutput.text = "Incorrect WHILE value"
                        }

                    }
                }
            }
            i++
        }
        //console.consoleOutput.text = console.consoleOutput.text.toString() + variables.toString()
    }

}