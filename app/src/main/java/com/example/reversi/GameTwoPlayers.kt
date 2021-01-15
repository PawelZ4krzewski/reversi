package com.example.reversi

import android.util.Log

class GameTwoPlayers(playerA: userPlayer, val playerB: userPlayer, board: Board, buttonSize: Int) :
    Game(playerA, board, buttonSize) {

    override fun accessibleMove() {
        if (!board.justFindEnemy(
                if (currentPlayer == playerA.color) playerA else playerB,
                if (currentPlayer == playerA.color) playerB else playerA
            )
        ) {
            currentPlayer =
                if (currentPlayer == playerA.color) playerB.color else playerA.color
            Log.d(
                "Game",
                "${if (currentPlayer == playerA.color) playerA.color else playerB.color} nie moze wykonac zadnego ruchu"
            )
        } else {
            Log.d(
                "Game",
                "${if (currentPlayer == playerA.color) playerA.color else playerB.color}  moze wykonac ruchu"
            )
        }
    }

    override fun isOver(): Boolean {
        if ((!board.justFindEnemy(playerA, playerB) && !board.justFindEnemy(playerB, playerA))
            || emptyFields == 0
            || playerA.amountOfPawns == 0
            || playerB.amountOfPawns == 0
        ) {
            Log.d("Game", "KONIEC GRY")
            return true
        }
        return false
    }
}