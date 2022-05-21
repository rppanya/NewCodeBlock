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
import com.example.codeblock1.BlockAdapter.BlocksHolder
import java.util.*
import kotlin.collections.ArrayList

class BlockAdapter :
    RecyclerView.Adapter<BlocksHolder>() { //private val testVal: test в конструктор

    val blocksList = ArrayList<Block>()

    inner class BlocksHolder(item: View) : RecyclerView.ViewHolder(item) {
        val blockType: TextView = item.findViewById(R.id.blockType)
        val value: EditText = item.findViewById(R.id.value)
        val name: EditText = item.findViewById(R.id.name)

        init {
            name.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    blocksList[adapterPosition].name = s.toString()
                }

                override fun afterTextChanged(s: Editable) {}
            })

            value.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    blocksList[adapterPosition].value = s.toString()
                }

                override fun afterTextChanged(s: Editable) {}

            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = when (blocksList[position].blockType) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlocksHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return BlocksHolder(view)
    }

    override fun onBindViewHolder(holder: BlocksHolder, position: Int) {
        holder.name.setText(blocksList[position].name)
        holder.value.setText(blocksList[position].value)
        holder.blockType.text = blocksList[position].blockType
    }


    fun removeBlock(position: Int) {
        if (blocksList[position].blockType == "IF") {
            var counter = 0
            for (i in position + 1 until blocksList.size) {
                if (blocksList[i].blockType == "IF") {
                    counter++
                } else if ((blocksList[i].blockType == "END_IF") && counter-- == 0) {
                    var elseIndex = -1
                    counter = 0
                    for (j in position + 1 until i) {
                        if (blocksList[j].blockType == "IF") {
                            counter++
                        }
                        if (blocksList[j].blockType == "END_IF") counter--
                        if (blocksList[j].blockType == "ELSE" && counter == 0) {
                            elseIndex = j
                            break
                        }
                    }
                    if (elseIndex != -1) {
                        blocksList.removeAt(elseIndex)
                        blocksList.removeAt(position)
                        blocksList.removeAt(i - 2)
                        notifyItemRemoved(elseIndex)
                        notifyItemRemoved(position)
                        notifyItemRemoved(i - 2)
                        break
                    } else {
                        blocksList.removeAt(position)
                        blocksList.removeAt(i - 1)
                        notifyItemRemoved(position)
                        notifyItemRemoved(i - 1)
                        break
                    }
                }
            }
        } else if (blocksList[position].blockType == "END_IF") {
            var counter = 0
            for (i in position - 1 downTo 0) {
                if (blocksList[i].blockType == "END_IF") {
                    counter++
                } else if (blocksList[i].blockType == "IF" && counter-- == 0) {
                    var elseIndex = -1
                    counter = 0
                    for (j in position - 1 downTo i + 1) {
                        if (blocksList[j].blockType == "END_IF") {
                            counter++
                        }
                        if (blocksList[j].blockType == "IF") counter--
                        if (blocksList[j].blockType == "ELSE" && counter == 0) {
                            elseIndex = j
                            break
                        }
                    }
                    if (elseIndex != -1) {
                        blocksList.removeAt(elseIndex)
                        blocksList.removeAt(i)
                        blocksList.removeAt(position - 2)
                        notifyItemRemoved(elseIndex)
                        notifyItemRemoved(i)
                        notifyItemRemoved(position - 2)
                        break
                    } else {
                        blocksList.removeAt(i)
                        blocksList.removeAt(position - 1)
                        notifyItemRemoved(i)
                        notifyItemRemoved(position - 1)
                        break
                    }

                }
            }
        } else if (blocksList[position].blockType == "WHILE") {
            var counter = 0
            for (i in position + 1 until blocksList.size) {
                if (blocksList[i].blockType == "WHILE") {
                    counter++
                } else if (blocksList[i].blockType == "END_WHILE" && counter-- == 0) {
                    blocksList.removeAt(position)
                    blocksList.removeAt(i - 1)
                    notifyItemRemoved(position)
                    notifyItemRemoved(i - 1)
                    break
                }
            }
        } else if (blocksList[position].blockType == "END_WHILE") {
            var counter = 0
            for (i in position - 1 downTo 0) {
                if (blocksList[i].blockType == "END_WHILE") {
                    counter++
                } else if (blocksList[i].blockType == "WHILE" && counter-- == 0) {
                    blocksList.removeAt(i)
                    blocksList.removeAt(position - 1)
                    notifyItemRemoved(i)
                    notifyItemRemoved(position - 1)
                    break
                }
            }
        } else {
            blocksList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addVarBlock(block: Block) {
        blocksList.add(block)
        notifyItemInserted(blocksList.size)
    }

    override fun getItemCount(): Int {
        return blocksList.size
    }

    fun callVarBlocksList(): ArrayList<Block> {
        return blocksList
    }

    /*fun callVariablesList(): ArrayList<VarValue>{
        return variables
    }*/

    var simpleCellback =
        object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val startPosition = viewHolder.adapterPosition
                val endPosition = target.adapterPosition

                Collections.swap(blocksList, startPosition, endPosition)
                recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)
                return true
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        }
}