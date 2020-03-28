package com.example.tictactoe

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        setContentView(R.layout.activity_main)


        val playWithCpuButton = findViewById<Button>(R.id.computerPlayButton)
        val playWitPlayerButton = findViewById<Button>(R.id.playerPlay)

        playWithCpuButton.setOnClickListener {
            val playWithCPU = true
            val intent = Intent(this, PlayingActivity::class.java).apply {
                putExtra("playWithCPU", playWithCPU.toString())
            }
            startActivity(intent)
        }

        playWitPlayerButton.setOnClickListener {
            val playWithCPU = false
            val intent = Intent(this, PlayingActivity::class.java).apply {
                putExtra("playWithCPU", playWithCPU.toString())
            }
            startActivity(intent)
        }

    }
}
