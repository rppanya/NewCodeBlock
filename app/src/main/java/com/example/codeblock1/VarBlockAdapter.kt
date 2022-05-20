package com.example.codeblock1

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.codeblock1.VarBlockAdapter.VarBlocksHolder
import java.util.*
import kotlin.collections.ArrayList

class VarBlockAdapter : RecyclerView.Adapter<VarBlocksHolder>() { //private val testVal: test в конструктор

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
            "END_IF" -> R.layout.end_if_block
            "WHILE" -> R.layout.while_block
            "END_WHILE" -> R.layout.end_while_block
            "ELSE" -> R.layout.end_if_plus_else_block
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


    fun removeBlock(position: Int){
        if (varBlocksList[position].blockType == "IF") {
            var counter = 0
            for (i in position+1 until varBlocksList.size) {
                if (varBlocksList[i].blockType == "IF") {
                    counter++
                } else if ((varBlocksList[i].blockType == "END_IF") && counter-- == 0) {
                    var elseIndex = -1
                    counter = 0
                    for (j in position+1 until i) {
                        if (varBlocksList[j].blockType == "IF") {
                            counter++
                        }
                        if (varBlocksList[j].blockType == "END_IF") counter--
                        if (varBlocksList[j].blockType == "ELSE" && counter == 0) {
                            elseIndex = j
                            break
                        }
                    }
                    if (elseIndex!=-1) {
                        varBlocksList.removeAt(elseIndex)
                        varBlocksList.removeAt(position)
                        varBlocksList.removeAt(i - 2)
                        notifyItemRemoved(elseIndex)
                        notifyItemRemoved(position)
                        notifyItemRemoved(i - 2)
                        break
                    } else {
                        varBlocksList.removeAt(position)
                        varBlocksList.removeAt(i-1)
                        notifyItemRemoved(position)
                        notifyItemRemoved(i-1)
                        break
                    }
                }
            }
        } else if (varBlocksList[position].blockType == "END_IF") {
            var counter = 0
            for (i in position-1 downTo 0) {
                if (varBlocksList[i].blockType == "END_IF" ) {
                    counter++
                } else if (varBlocksList[i].blockType == "IF" && counter-- == 0) {
                    var elseIndex = -1
                    counter = 0
                    for (j in position-1 downTo i+1) {
                        if (varBlocksList[j].blockType == "END_IF") {
                            counter++
                        }
                        if (varBlocksList[j].blockType == "IF") counter--
                        if (varBlocksList[j].blockType == "ELSE" && counter == 0) {
                            elseIndex = j
                            break
                        }
                    }
                    if (elseIndex!=-1) {
                        varBlocksList.removeAt(elseIndex)
                        varBlocksList.removeAt(i)
                        varBlocksList.removeAt(position - 2)
                        notifyItemRemoved(elseIndex)
                        notifyItemRemoved(i)
                        notifyItemRemoved(position - 2)
                        break
                    } else {
                        varBlocksList.removeAt(i)
                        varBlocksList.removeAt(position-1)
                        notifyItemRemoved(i)
                        notifyItemRemoved(position-1)
                        break
                    }

                }
            }
        } else if (varBlocksList[position].blockType == "WHILE") {
            var counter = 0
            for (i in position+1 until varBlocksList.size) {
                if (varBlocksList[i].blockType == "WHILE") {
                    counter++
                } else if (varBlocksList[i].blockType == "END_WHILE" && counter-- == 0) {
                    varBlocksList.removeAt(position)
                    varBlocksList.removeAt(i-1)
                    notifyItemRemoved(position)
                    notifyItemRemoved(i-1)
                    break
                }
            }
        } else if (varBlocksList[position].blockType == "END_WHILE") {
            var counter = 0
            for (i in position-1 downTo 0) {
                if (varBlocksList[i].blockType == "END_WHILE") {
                    counter++
                } else if (varBlocksList[i].blockType == "WHILE" && counter-- == 0) {
                    varBlocksList.removeAt(i)
                    varBlocksList.removeAt(position-1)
                    notifyItemRemoved(i)
                    notifyItemRemoved(position-1)
                    break
                }
            }
        } else {
            varBlocksList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addVarBlock(block: VarBlock){
        varBlocksList.add(block)
        notifyItemInserted(varBlocksList.size)
    }

    override fun getItemCount(): Int {
        return varBlocksList.size
    }

    fun callVarBlocksList(): ArrayList<VarBlock>{
        return varBlocksList
    }

    /*fun callVariablesList(): ArrayList<VarValue>{
        return variables
    }*/

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


        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    }
}