package com.example.codeblock1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codeblock1.databinding.ActivityBlocksBinding
import com.example.codeblock1.databinding.ConsolePageBinding


private var canCallConsole = true



class BlocksActivity : Activity() {
    private var clicked = false
    lateinit var binding: com.example.codeblock1.databinding.ActivityBlocksBinding
    private val adapter = VarBlockAdapter()

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlocksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init(this)

        binding.addButton.setOnClickListener {
            val rotateOpen: Animation by lazy {
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.rotate_open_anim
                )
            }
            val rotateClose: Animation by lazy {
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.rotate_close_anim
                )
            }
            val fromBottom: Animation by lazy {
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.from_bottom_anim
                )
            }
            val toBottom: Animation by lazy {
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.to_bottom_anim
                )
            }
            if (!clicked) {
                binding.addButton.startAnimation(rotateOpen)
                binding.menu.visibility = View.VISIBLE
                binding.btnVariables.startAnimation(fromBottom)
                binding.btnIf.startAnimation(fromBottom)
                binding.btnIfElse.startAnimation(fromBottom)
                binding.btnWhile.startAnimation(fromBottom)
                binding.btnPrint.startAnimation(fromBottom)

                binding.textView2.startAnimation(fromBottom)
                binding.textView3.startAnimation(fromBottom)
                binding.textView4.startAnimation(fromBottom)
                binding.textView5.startAnimation(fromBottom)
                binding.textView6.startAnimation(fromBottom)
                clicked = true
            } else {
                binding.addButton.startAnimation(rotateClose)

                clicked = false
                /*binding.btnVariables.startAnimation(toBottom)
                binding.btnIfElse.startAnimation(toBottom)
                binding.btnPrint.startAnimation(toBottom)

                binding.textView2.startAnimation(toBottom)
                binding.textView4.startAnimation(toBottom)
                binding.textView5.startAnimation(toBottom)*/


                binding.menu.visibility = View.GONE
            }
        }


        val view = LayoutInflater.from(this).inflate(R.layout.console_page, null)

        binding.btnDebug.setOnClickListener {
            val varBlocksList = adapter.callVarBlocksList()
            val console = com.example.codeblock1.databinding.ConsolePageBinding.bind(view)
            //val variables = adapter.callVariablesList()
            //run(varBlocksList, console)
            val current = Run()
            current.run(varBlocksList, console)
        }

        binding.consoleButton.setOnClickListener {
            if (canCallConsole) {
                binding.layoutConsole.addView(view)
                canCallConsole = false
                binding.addButton.hide()

            } else {
                binding.layoutConsole.removeAllViews()
                canCallConsole = true
                binding.addButton.show()
            }

        }
        binding.backButton.setOnClickListener{
            val current = Intent(this@BlocksActivity, MainActivity::class.java)
            startActivity(current)
        }
    }

    private fun init(context: Context) {
        binding.apply{
            rcView.layoutManager = LinearLayoutManager(this@BlocksActivity)
            rcView.adapter = adapter
            val itemTouchHelper = ItemTouchHelper(adapter.simpleCellback)
            itemTouchHelper.attachToRecyclerView(rcView)

            val swipeHandler = object: SwipeToDelete(context) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val swipeAdapter = rcView.adapter as VarBlockAdapter
                    swipeAdapter.removeBlock(viewHolder.adapterPosition)
                }
            }
            val itemTouchHelperSwipe = ItemTouchHelper(swipeHandler)
            itemTouchHelperSwipe.attachToRecyclerView(rcView)

            binding.btnVariables.setOnClickListener{
                val block = VarBlock("", "", "VAR")
                adapter.addVarBlock(block)

            }

            binding.btnPrint.setOnClickListener{
                val block = VarBlock("", "", "PRINT")
                adapter.addVarBlock(block)
            }

            binding.btnIfElse.setOnClickListener{
                var block = VarBlock("", "", "IF")
                adapter.addVarBlock(block)
                block = VarBlock("", "", "END_IF")
                adapter.addVarBlock(block)
            }
            binding.btnWhile.setOnClickListener{
                var block = VarBlock("", "", "WHILE")
                adapter.addVarBlock(block)
                block = VarBlock("", "", "END_WHILE")
                adapter.addVarBlock(block)
            }
        }
    }
}


