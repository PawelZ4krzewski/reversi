package com.example.reversi

import android.util.Log

class Game(playerA: Player, playerB: Player, board: Board, buttonSize: Int) {

    val playerA = playerA
    val playerB = playerB
    val board = board
    val buttonSize = buttonSize
    var emptyFields = board.boardSize * board.boardSize - 4
    var currentPlayer = 'B'

    init {
        Log.d("Game", "Utworzono klase Game")
    }
    fun accessibleMove(){
        if (!board.justFindEnemy(
                if (currentPlayer == playerA.color) playerA else playerB,
                if (currentPlayer == playerA.color) playerB else playerA
            )
        ) {
            currentPlayer =
                if (currentPlayer == playerA.color) playerB.color else playerA.color
            Log.d("Game", "${if (currentPlayer == playerA.color) playerA.color else playerB.color} nie moze wykonac zadnego ruchu")
        } else {
            Log.d(
                "Game",
                "${if (currentPlayer == playerA.color) playerA.color else playerB.color}  moze wykonac zadnego ruchu"
            )
        }
    }

    fun isOver() : Boolean
    {
        if((!board.justFindEnemy(playerA, playerB) && !board.justFindEnemy(playerB, playerA)) || emptyFields == 0)
        {
            Log.d("Game","KONIEC GRY")
            return true
        }
        return false
    }
}