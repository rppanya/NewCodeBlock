package com.example.codeblock1

import android.annotation.SuppressLint
import com.example.codeblock1.databinding.ConsolePageBinding

class Run {

    private val variables = ArrayList<VarValue>()
    private val stackForIf = ArrayList<Int>()
    private val stackForWhile = ArrayList<Int>()


    private fun variablesValidness(
        varStr: String,
        blocksList: ArrayList<Block>,
        index: Int
    ): String {
        val variablePattern =
            Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = varStr)
        variablePattern.forEach { variable ->
            var varInd: Int = -1
            var varValue = ""
            for (i in 0 until blocksList.size) {
                if (blocksList[i].name == variable.value && blocksList[i].blockType == "VAR") {
                    varInd = i
                    varValue = blocksList[i].value
                    break
                }
            }
            if (varInd > index || varInd == -1 || varValue == "") {
                return variable.value
            }
        }
        return "-1"
    }

    private fun convertVarToNum(varName: String, variables: ArrayList<VarValue>): String {
        variables.forEach { block ->
            if (block.name == varName) {
                if (isVariableInString(block.value)) {
                    var nameCopy: String = block.value
                    var pattern: Sequence<MatchResult> =
                        Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = block.value)
                    pattern = pattern.sortedBy { -it.value.length }
                    pattern.forEach {
                        val number = convertVarToNum(it.value, variables)
                        nameCopy = nameCopy.replace(it.value, number)
                    }
                    val exception = ReversePolishNotation(nameCopy)
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

    private fun isDebugValid(blocksList: ArrayList<Block>): String {
        blocksList.forEach { block ->
            block.name = block.name.replace("\\s".toRegex(), "")
            block.value = block.value.replace("\\s".toRegex(), "")
            when (block.blockType) {
                "VAR" -> {
                    //name field
                    val incorrectVar = Regex(pattern = """^([^a-zA-Z])|[^\w\d_]|_$""".trimIndent())
                    if (incorrectVar.containsMatchIn(block.name)) {
                        return "Incorrect name of variable: ${block.name}"
                    }

                    //value field: variable check
                    val incorrect =
                        variablesValidness(block.value, blocksList, blocksList.indexOf(block))
                    if (incorrect != "-1") {
                        return "Can't find variable: $incorrect in VAR"
                    }

                    //value field: signs check
                    val incorrectSigns = Regex(pattern = """[-+*/%][-+*/%]+""".trimIndent())
                    if (incorrectSigns.containsMatchIn(block.value)) {
                        return "Incorrect arithmetic expression: ${block.value}"
                    }

                }

                "PRINT" -> {
                    val incorrect =
                        variablesValidness(block.name, blocksList, blocksList.indexOf(block))
                    if (incorrect != "-1") {
                        return "Can't find variable $incorrect in PRINT"
                    }
                }


                "IF" -> {
                    val ifPattern =
                        Regex("""((((([a-z]|[A-Z])[\w\d_]*)|(\d+)))([+\-/*%](((([a-z]|[A-Z])[\w\d_]*)|(\d+))))*)(>|<|>=|<=|==|!=)((((([a-z]|[A-Z])[\w\d_]*)|(\d+)))([+\-\/*%](((([a-z]|[A-Z])[\w\d_]*)|(\d+))))*)""".trimIndent()).findAll(
                            input = block.name
                        )
                    var flag = false
                    ifPattern.forEach {
                        flag = true
                        if (it.value.length != block.name.length) {
                            return "Incorrect IF expression"
                        }
                    }
                    if (!flag) {
                        return "Incorrect IF expression"
                    }

                    val incorrect =
                        variablesValidness(block.name, blocksList, blocksList.indexOf(block))
                    if (incorrect != "-1") {
                        return "Can't find variable $incorrect in IF"
                    }
                }
                "WHILE" -> {
                    val ifPattern =
                        Regex("""((((([a-z]|[A-Z])[\w\d_]*)|(\d+)))([+\-/*%](((([a-z]|[A-Z])[\w\d_]*)|(\d+))))*)(>|<|>=|<=|==|!=)((((([a-z]|[A-Z])[\w\d_]*)|(\d+)))([+\-\/*%](((([a-z]|[A-Z])[\w\d_]*)|(\d+))))*)""".trimIndent()).findAll(
                            input = block.name
                        )
                    var flag = false

                    if (!isVariableInString(block.name)) {
                        return "Can't find variables in WHILE"
                    }

                    ifPattern.forEach {
                        flag = true
                        if (it.value.length != block.name.length) {
                            return "Incorrect WHILE expression"
                        }
                    }
                    if (!flag) {
                        return "Incorrect WHILE expression"
                    }

                    val incorrect =
                        variablesValidness(block.name, blocksList, blocksList.indexOf(block))
                    if (incorrect != "-1") {
                        return "Can't find variable $incorrect in WHILE"
                    }

                }
            }
        }
        return "Correct"
    }

    private fun findVarInd(nameOfVar: String, variables: ArrayList<VarValue>): Int {
        for (i in 0 until variables.size) {
            if (variables[i].name == nameOfVar) {
                return i
            }
        }
        return -1
    }

    private fun varBeforeIf(
        blocksList: ArrayList<Block>,
        start: Int,
        finish: Int,
        name: String
    ): Boolean {
        for (i in finish downTo start) {
            if (blocksList[i].blockType == "VAR" && blocksList[i].name == name) {
                return true
            }
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    fun run(blocksList: ArrayList<Block>, console: ConsolePageBinding) {

        var counterWhile = 0
        stackForIf.clear()
        variables.clear()
        val correctness = isDebugValid(blocksList)

        if (correctness != "Correct") {
            console.consoleOutput.text = correctness
            return
        }

        console.consoleOutput.text = ""
        var i = 0
        while (i < blocksList.size) {
            blocksList[i].name = blocksList[i].name.replace("\\s".toRegex(), "")
            blocksList[i].value = blocksList[i].value.replace("\\s".toRegex(), "")
            when (blocksList[i].blockType) {
                "VAR" -> {
                    val nameCopy: String = blocksList[i].name
                    val valueCopy: String = blocksList[i].value
                    var indexIfVarNotAvailable = 0

                    if (nameCopy != "") {
                        indexIfVarNotAvailable = findVarInd(nameCopy, variables)

                        if (indexIfVarNotAvailable != -1) {
                            var flag = false
                            val varPattern: Sequence<MatchResult> =
                                Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = blocksList[i].value)
                            varPattern.forEach {
                                if (it.value == variables[indexIfVarNotAvailable].name) {
                                    val number = convertVarToNum(it.value, variables)
                                    variables[indexIfVarNotAvailable].value = blocksList[i].value
                                    variables[indexIfVarNotAvailable].value =
                                        variables[indexIfVarNotAvailable].value.replace(
                                            it.value,
                                            number
                                        )
                                    flag = true
                                }
                            }
                            if (!flag) {
                                variables[indexIfVarNotAvailable].value = blocksList[i].value
                            }

                        } else {
                            variables.add(VarValue(nameCopy, valueCopy))
                        }
                    }

                }
                "PRINT" -> {
                    var nameCopy: String = blocksList[i].name
                    if (isVariableInString(blocksList[i].name)) {
                        var pattern: Sequence<MatchResult> =
                            Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = blocksList[i].name)
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

                    var nameCopy: String = blocksList[i].name
                    if (isVariableInString(blocksList[i].name)) {
                        var pattern: Sequence<MatchResult> =
                            Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = blocksList[i].name)
                        pattern = pattern.sortedBy { -it.value.length }
                        pattern.forEach {
                            val number = convertVarToNum(it.value, variables)
                            nameCopy = nameCopy.replace(it.value, number)
                        }
                    }
                    val condition = InterpreterForInequalities(nameCopy)
                    if (!condition.interpretInequality()) {
                        var skip = 0
                        var counter = 0

                        for (j in i + 1 until blocksList.size) {
                            if (blocksList[j].blockType == "IF") {
                                counter++
                            } else if (blocksList[j].blockType == "END_IF" && counter-- == 0) {
                                break
                            } else {
                                skip++
                            }
                        }
                        i += (skip + 1)
                    }
                }
                "ELSE" -> {
                    var counterIfs = 0
                    for (j in i - 1 downTo 0) {
                        if (blocksList[j].blockType == "END_IF") {
                            counterIfs++
                        } else if (blocksList[j].blockType == "IF" && counterIfs-- == 0) {
                            var nameCopy: String = blocksList[j].name
                            if (isVariableInString(blocksList[j].name)) {
                                var pattern: Sequence<MatchResult> =
                                    Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = blocksList[j].name)
                                pattern = pattern.sortedBy { -it.value.length }
                                pattern.forEach {
                                    val number = convertVarToNum(it.value, variables)
                                    nameCopy = nameCopy.replace(it.value, number)
                                }
                            }
                            val condition = InterpreterForInequalities(nameCopy)
                            if (condition.interpretInequality()) {
                                var skip = 0
                                var counter = 0

                                for (k in i + 1 until blocksList.size) {
                                    if (blocksList[k].blockType == "IF") {
                                        counter++
                                    } else if (blocksList[k].blockType == "END_IF" && counter-- == 0) {
                                        break
                                    } else {
                                        skip++
                                    }
                                }
                                i += skip
                            }
                        }
                    }

                }

                "END_IF" -> {
                    if (stackForIf.isEmpty()) {
                        console.consoleOutput.text = "Invalid IF operator brackets"
                        return
                    } else {
                        for (j in i - 1 downTo stackForIf.last()) {
                            if (blocksList[j].blockType == "VAR") {
                                if (!varBeforeIf(
                                        blocksList,
                                        0,
                                        stackForIf.last(),
                                        blocksList[j].name
                                    )
                                ) {

                                    for (k in 0 until variables.size) {
                                        if (variables[k].name == blocksList[j].name) {
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
                    var nameCopy: String = blocksList[i].name
                    if (isVariableInString(blocksList[i].name)) {
                        var pattern: Sequence<MatchResult> =
                            Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = blocksList[i].name)
                        pattern = pattern.sortedBy { -it.value.length }
                        pattern.forEach {
                            val number = convertVarToNum(it.value, variables)
                            nameCopy = nameCopy.replace(it.value, number)
                        }
                    }
                    val condition = InterpreterForInequalities(nameCopy)
                    if (!condition.interpretInequality()) {
                        var skip = 0
                        for (j in i + 1 until blocksList.size) {
                            if (blocksList[j].blockType != "END_WHILE") {
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

                        var nameCopy: String = blocksList[stackForWhile.last()].name
                        if (isVariableInString(blocksList[stackForWhile.last()].name)) {
                            var pattern: Sequence<MatchResult> =
                                Regex("""([a-z]|[A-Z])[\w\d_]*""".trimIndent()).findAll(input = blocksList[stackForWhile.last()].name)
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
    }

}