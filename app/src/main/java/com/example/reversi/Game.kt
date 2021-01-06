package com.example.reversi

import android.util.Log

class Game(playerA: Player, playerB: Player, board: Board, buttonSize: Int ) {

    val playerA = playerA
    val playerB = playerB
    val board = board
    val buttonSize = buttonSize
    var currentPlayer = 'B'

    init {
        Log.d("Game","Utworzono klase Game")
    }
}