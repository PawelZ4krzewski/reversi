package com.example.reversi

import android.widget.TextView

class Game(var amountOfPlayer: Int) {

    private var winner: String = ""
        get() {
            return field
        }
        set(value) {
            field = value
        }
    val score = IntArray(2)
    val scoreTextView = mutableListOf<TextView>()

    fun addScore(textView: TextView)
    {
        scoreTextView.add(textView)
    }

    fun isOver(boardSize: Int): Boolean
    {
        if(boardSize*boardSize == score[0]+score[1])
        {
            return true
        }
        return false
    }

}