package com.example.codeblock1
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.codeblock1.databinding.ActivityInstructionsBinding

class InstructionsActivity : Activity() {
    private lateinit var binding: ActivityInstructionsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val current = Intent(this@InstructionsActivity, MainActivity::class.java)
            startActivity(current)
        }

        binding.btnTranslate.setOnClickListener {

        }
    }

}