package com.example.codeblock1

import android.view.View
import com.example.codeblock1.databinding.VariablesBlockBinding

data class VarBlock(var name: String, var value: String, val blockType: String, var layoutBlock: VariablesBlockBinding){}
