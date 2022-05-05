package com.example.codeblock1

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.codeblock1.databinding.CodePageBinding
import com.example.codeblock1.databinding.ConsolePageBinding
import com.example.codeblock1.databinding.VariablesBlockBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import java.util.*
import kotlin.collections.ArrayList

class VarBlockAdapter : RecyclerView.Adapter<VarBlockAdapter.VarBlocksHolder>() { //private val testVal: test в конструктор

    private val varBlocksList = ArrayList<VarBlock>()

    class VarBlocksHolder(item: View):RecyclerView.ViewHolder(item){
        val blockType : TextView = item.findViewById(R.id.nameOfBlock)
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = when(varBlocksList[position].blockType){
            "PRINT" -> R.layout.print_block
            "IF" -> R.layout.if_block
            else -> R.layout.variables_block
        }
        return viewType;
    }
//    class VarBlocksHolder(item: View):RecyclerView.ViewHolder(item){
//        private val binding = VariablesBlockBinding.bind(item)
//        fun bind(block: VarBlock) = with(binding){
//            when(block.blockType) {
//                "PRINT" -> {
//                    valueOfVariable.visibility = View.GONE
//                    nameOfBlock.text = block.blockType
//                    blockForVariable.setBackgroundResource(R.color.printBlockColor)
//                }
//                "IF" -> {
//                    nameOfBlock.text = block.blockType
//                    valueOfVariable.visibility = View.GONE
//                    nameOfVariable.hint = "CONDITION"
//                    blockForVariable.setBackgroundResource(R.color.ifBlockColor)
//                }
//            }
//        }
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VarBlocksHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return VarBlocksHolder(view)
    }

    override fun onBindViewHolder(holder: VarBlocksHolder, position: Int) {
//        holder.bind(varBlocksList[position])
        holder.blockType.text = varBlocksList[position].blockType
    }

    override fun getItemCount(): Int {
        return varBlocksList.size
    }

    //testVal.func() - так!!



    @SuppressLint("NotifyDataSetChanged")
    fun addVarBlock(block: VarBlock){
        varBlocksList.add(block)
        notifyDataSetChanged()
    }

    fun callVarBlocksList(): ArrayList<VarBlock>{
        return varBlocksList
    }
    /*interface test{
        fun func() {}
 //в блоксактивити нужно оверрайднуть
    }*/
    var simpleCellback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),0) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            var startPosition = viewHolder.adapterPosition
            var endPosition = target.adapterPosition

            Collections.swap(varBlocksList, startPosition, endPosition)
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

    }

}