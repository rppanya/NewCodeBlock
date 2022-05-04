package com.example.codeblock1

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.example.codeblock1.databinding.ConsolePageBinding
import com.example.codeblock1.databinding.VariablesBlockBinding

class VarBlockAdapter : RecyclerView.Adapter<VarBlockAdapter.VarBlocksHolder>() {
    open val varBlocksList = ArrayList<VarBlock>()
    class VarBlocksHolder(item: View):RecyclerView.ViewHolder(item){
        val binding = VariablesBlockBinding.bind(item)
        fun bind(block: VarBlock) = with(binding){
            when(block.blockType) {
                "PRINT" -> {
                    valueOfVariable.visibility = View.GONE
                    nameOfBlock.text = block.blockType
                    blockForVariable.setBackgroundResource(R.color.printBlockColor)
                }
                "IF" -> {
                    nameOfBlock.text = block.blockType
                    valueOfVariable.visibility = View.GONE
                    nameOfVariable.hint = "CONDITION"
                    blockForVariable.setBackgroundResource(R.color.ifBlockColor)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VarBlocksHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.variables_block, parent, false)
        return VarBlocksHolder(view)
    }

    override fun onBindViewHolder(holder: VarBlocksHolder, position: Int) {
        holder.bind(varBlocksList[position])
    }

    override fun getItemCount(): Int {
        return varBlocksList.size
    }

    fun swap(firstPosition: Int, secondPosition: Int){
        val x = varBlocksList[firstPosition]
        varBlocksList[firstPosition] = varBlocksList[secondPosition]
        varBlocksList[secondPosition] = x
        notifyItemMoved(firstPosition, secondPosition);
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addVarBlock(block: VarBlock){
        varBlocksList.add(block)
        notifyDataSetChanged()
    }

    fun callVarBlocksList(): ArrayList<VarBlock>{
        return varBlocksList
    }
}