package com.example.tictactoe

import GameValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import TicTacToe
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.content.res.ResourcesCompat
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView


class PlayingActivity : AppCompatActivity() {

    var ticTacToe = TicTacToe()
    lateinit var moveTextView: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar!!.hide()

        setContentView(R.layout.activity_playing)

        moveTextView = findViewById<TextView>(R.id.move_textview)
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

                    if (playerNames.size == 1)
                        ticTacToe.initializeTicTac(playWithCPU, playerNames[0], null)
                    else
                        ticTacToe.initializeTicTac(playWithCPU, playerNames[0], playerNames[1])

                    startGame(applicationContext)
                })
            this.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })
        }

        if (playWithCPU)
            builder?.setMessage("Please put your name")!!.setTitle("Player registration")
        else
            builder?.setMessage("Please put your names, like:\nFirst, Second")!!.setTitle("Player registration")
        val alertDialog = builder.create()

        playerNames.clear()
        alertDialog.show()

    }

    private fun startGame(context: Context) {
        setAllBlank()
        moveTextView.text =
            "${ticTacToe.currentPlayer.name} move (${ticTacToe.currentPlayer.gameValue})"
    }


    /**
     * This function is called when one of game field was clicked.
     * Non empty field exception handled
     */
    fun fieldClicked(view: View) {

        val cord = view.tag.toString() + ticTacToe.currentPlayer.gameValue.toString()

        val circleBool = isDifferentImages(R.drawable.circle, view)
        val xBool = isDifferentImages(R.drawable.x_mark, view)

        putValueField(circleBool, xBool, view, R.drawable.x_mark, GameValues.X)
        putValueField(circleBool, xBool, view, R.drawable.circle, GameValues.O)

        ticTacToe.playGame(cord)

        if (ticTacToe.currentPlayer.name == "Computer" && !ticTacToe.isGameEnd && !ticTacToe.winner.win) {
            val only = ticTacToe.drawCords()?.split("O")?.get(0)
            val imageID = "image_$only"
            val resID = resources.getIdentifier(imageID, "id", packageName)
            fieldClicked(findViewById<ImageView>(resID))
        }
        moveTextView.text =
            "${ticTacToe.currentPlayer.name} move (${ticTacToe.currentPlayer.gameValue})"

        checkAndFinishIntent(
            ticTacToe.isGameEnd && ticTacToe.winner.win,
            "${ticTacToe.currentPlayer.name} won game!",
            moveTextView
        )
        checkAndFinishIntent(
            ticTacToe.isGameEnd && !ticTacToe.winner.win,
            "Nobody won this game, DRAW!",
            moveTextView
        )

    }

    private fun checkAndFinishIntent(expression: Boolean, message: String, view: View) {
        if (expression) {
            (view as TextView).text = message
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG
            ).show()

            Handler().postDelayed({
                finish()
            }, 3000)
        }
    }

    /**
     * If all expressions conditions fulfilled, image will be set to specific view
     */
    private fun putValueField(
        firstExpr: Boolean,
        secondExpr: Boolean,
        view: View,
        checkedDrawableID: Int,
        gameValue: GameValues
    ) {
        if (ticTacToe.currentPlayer.gameValue == gameValue) {
            if (firstExpr && secondExpr)
                (view as ImageView).setImageResource(checkedDrawableID)
            else
                Log.d("ComputerPlay", "Please put value in non empty slot")
        }
    }

    /**
     * Check if drawable image is in input view
     */
    private fun isDifferentImages(drawableID: Int, view: View): Boolean {
        return (view as ImageView).drawable.constantState != ResourcesCompat.getDrawable(
            resources,
            drawableID,
            null
        )?.constantState
    }

    private fun setAllBlank() {
        val images = getAllFields()
        images.map {
            it.setImageResource(R.drawable.blank)
        }
    }

    private fun getAllFields(): MutableCollection<ImageView> {
        return mutableListOf(
            findViewById(R.id.image_11), findViewById(R.id.image_12), findViewById(R.id.image_13),
            findViewById(R.id.image_21), findViewById(R.id.image_22), findViewById(R.id.image_23),
            findViewById(R.id.image_31), findViewById(R.id.image_32), findViewById(R.id.image_33)
        )
    }

}
