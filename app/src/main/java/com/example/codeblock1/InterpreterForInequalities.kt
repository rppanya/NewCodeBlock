package com.example.codeblock1

import java.lang.Integer.parseInt

class InterpreterForInequalities(var dataString: String) {
    var firstSubstring = ""
    var secondSubstring = ""
    var inequalitySign = ""
    private fun booleanValue(sign: String, firstValue: Int, secondValue: Int): Boolean {
        return when (sign) {
            ">" -> firstValue>secondValue
            "<" -> firstValue<secondValue
            "==" -> firstValue==secondValue
            "!=" -> firstValue!=secondValue
            ">=" -> firstValue>=secondValue
            "<=" -> firstValue<=secondValue
            else -> false
        }
    }
    private fun isInequalitySign(c: Char): Boolean { // возвращяем тру если один из символов ниже
        return c == '>' || c == '<' || c == '=' || c == '!'
    }


    fun interpretInequality(): Boolean {

        var flag = false
        for (i in dataString.indices) {
            if ( isInequalitySign(dataString[i]) ) {
                flag = true
                inequalitySign += dataString[i]
            } else if (!flag) {
                firstSubstring += dataString[i]
            } else {
                secondSubstring += dataString[i]
            }
        }
        val firstValue = parseInt(ReversePolishNotation(firstSubstring).RPN())
        val secondValue = parseInt(ReversePolishNotation(secondSubstring).RPN())

        return booleanValue(inequalitySign, firstValue, secondValue)
    }
}