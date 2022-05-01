package com.example.codeblock1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codeblock1.databinding.VariablesBlockBinding

class VarBlockAdapter : RecyclerView.Adapter<VarBlockAdapter.VarBlocksHolder>() {
    val varBlocksList = ArrayList<VarBlock>()
    class VarBlocksHolder(item: View):RecyclerView.ViewHolder(item){
        val binding = VariablesBlockBinding.bind(item)
        fun bind(block: VarBlock) = with(binding){
            nameOfBlock.text = block.name
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

    open fun swap(firstPosition: Int, secondPosition: Int){
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
}