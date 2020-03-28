package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar!!.hide()

        setContentView(R.layout.activity_main)

        val playWithCpuButton = findViewById<Button>(R.id.computerPlayButton)
        val playWitPlayerButton = findViewById<Button>(R.id.playerPlay)
        val intent = Intent(this, PlayingActivity::class.java)

        addListenerIntent(playWithCpuButton,intent,true,"playWithCPU")
        addListenerIntent(playWitPlayerButton,intent,false,"playWithCPU")

    }

    private fun addListenerIntent(view: View, intent: Intent, message: Boolean, messStr : String) {
        (view as Button).setOnClickListener {
            intent.putExtra(messStr, message.toString())
            startActivity(intent)
        }
    }

}
