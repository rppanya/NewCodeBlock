package com.example.codeblock1

class ReversePolishNotation(var dataString: String) {
    private fun processOperator(st: ArrayList<Int>, op: Char) {
        val r: Int = st.removeLast()
        val l: Int = st.removeLast()
        when (op) {
            '+' -> st.add(l + r)
            '-' -> st.add(l - r)
            '*' -> st.add(l * r)
            '/' -> st.add(l / r)
            '%' -> st.add(l % r)
        }
    }

    private fun isOperator(c: Char): Boolean { // возвращаем тру если один из символов ниже
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%'
    }

    private fun priority(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/', '%' -> 2
            else -> -1
        }
    }

    fun RPN(): String {
        val stackNumbers: ArrayList<Int> = ArrayList()
        val stackOperators: ArrayList<Char> = ArrayList()
        var i = 0
        while (i < dataString.length) {
            val current: Char = dataString[i]
            when {
                current == '(' -> {
                    stackOperators.add('(')
                    i++
                }
                current == ')' -> {
                    while (stackOperators.last() != '(') {
                        processOperator(stackNumbers, stackOperators.removeLast())
                    }
                    stackOperators.removeLast()
                    i++
                }
                isOperator(current) -> {
                    while (stackOperators.isNotEmpty() && priority(stackOperators.last()) >= priority(
                            current
                        )
                    ) {
                        processOperator(stackNumbers, stackOperators.removeLast())
                    }
                    stackOperators.add(current)
                    i++

                }
                else -> {
                    var operand = ""
                    while (i < dataString.length && Character.isDigit(dataString[i])) {
                        operand += dataString[i++]
                    }
                    stackNumbers.add(Integer.parseInt(operand))
                }
            }
        }
        while (stackOperators.isNotEmpty()) {
            processOperator(stackNumbers, stackOperators.removeLast())
        }
        return stackNumbers[0].toString()
    }
}