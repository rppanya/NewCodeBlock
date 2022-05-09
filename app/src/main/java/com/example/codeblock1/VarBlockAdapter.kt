package com.example.codeblock1

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class VarBlockAdapter : RecyclerView.Adapter<VarBlockAdapter.VarBlocksHolder>() { //private val testVal: test в конструктор

    val varBlocksList = ArrayList<VarBlock>()

    inner class VarBlocksHolder(item: View):RecyclerView.ViewHolder(item){
        val blockType : TextView = item.findViewById(R.id.nameOfBlock)
        val value : EditText = item.findViewById(R.id.valueOfVariable)
        val name : EditText = item.findViewById(R.id.nameOfVariable)

        init {
            name.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    varBlocksList[adapterPosition].name = s.toString()
                }

                override fun afterTextChanged(s: Editable) {}
            })

            value.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    varBlocksList[adapterPosition].value = s.toString()
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = when(varBlocksList[position].blockType){
            "PRINT" -> R.layout.print_block
            "IF" -> R.layout.if_block
            else -> R.layout.variables_block
        }
        return viewType;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VarBlocksHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return VarBlocksHolder(view)
    }

    override fun onBindViewHolder(holder: VarBlocksHolder, position: Int) {
//        holder.bind(varBlocksList[position])
        holder.name.setText(varBlocksList[position].name)
        holder.value.setText(varBlocksList[position].value)
        holder.blockType.text = varBlocksList[position].blockType
    }

    override fun getItemCount(): Int {
        return varBlocksList.size
    }

    //testVal.func() - так!!

    fun removeAt(position: Int) {
        varBlocksList.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addVarBlock(block: VarBlock){
        varBlocksList.add(block)
        notifyDataSetChanged()
    }

    fun callVarBlocksList(): ArrayList<VarBlock>{
        return varBlocksList
    }

    var simpleCellback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),0) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition

            Collections.swap(varBlocksList, startPosition, endPosition)
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)
            return true
        }


        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }



    }
}