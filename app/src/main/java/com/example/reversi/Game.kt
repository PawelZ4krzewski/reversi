package com.example.reversi

import android.util.Log

abstract class Game(var playerA: userPlayer, val board: Board, var buttonSize: Int) {

    var emptyFields = board.boardSize * board.boardSize - 4
    var currentPlayer = 'B'


    init {
        Log.d("Game", "Utworzono klase Game")
    }

    abstract fun accessibleMove()

    abstract fun isOver() : Boolean
}