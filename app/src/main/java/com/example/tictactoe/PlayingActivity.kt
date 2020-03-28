package com.example.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import TicTacToe
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.InputType
import android.util.Log
import android.view.FocusFinder
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class PlayingActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_playing)


        var ticTacToe: TicTacToe = TicTacToe()

        val playWithCPU = intent.getStringExtra("playWithCPU")!!.toBoolean()


        val builder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }

        val input: EditText = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT

        val playerNames = mutableListOf<String>()
        builder.apply {
            this!!.setView(input)
            this.setPositiveButton("Add",
                DialogInterface.OnClickListener { dialog, id ->
                    if (playWithCPU)
                        playerNames.add(input.text.toString())
                    else {
                        val str = input.text.toString()
                        playerNames.addAll(str.split(", "))
                    }
                    playerNames.map {
                        Log.d("PLAYERNAMES", it)
                        Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                    }
                    if (playerNames.size == 1)
                        ticTacToe.initializeTicTac(playWithCPU, playerNames[0], null)
                    else
                        ticTacToe.initializeTicTac(playWithCPU, playerNames[0], playerNames[1])

                    startGame()
                })
            this.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        }

        builder?.setMessage("Please put player/players name")!!.setTitle("User name")
        val alertDialog = builder.create()

        playerNames.clear()
        alertDialog.show()



    }

    private fun startGame(){

    }
}
