package com.example.codeblock1

import android.os.Binder
import android.renderscript.ScriptGroup
import android.view.View
import com.example.codeblock1.databinding.IfBlockBinding
import com.example.codeblock1.databinding.VariablesBlockBinding
import java.net.BindException

data class VarBlock(var name: String, var value: String, val blockType: String){}
