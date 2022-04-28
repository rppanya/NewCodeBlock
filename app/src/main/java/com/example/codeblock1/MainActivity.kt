package com.example.codeblock1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        begin_button.setOnClickListener {
            val current = Intent(this@MainActivity, BlocksActivity::class.java)
            startActivity(current)
        }
        exit_button.setOnClickListener {
            finishAffinity()
        }
    }

}