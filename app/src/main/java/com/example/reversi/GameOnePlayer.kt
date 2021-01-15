package com.example.reversi

import android.util.Log

class GameOnePlayer(playerA: userPlayer, bot: Bot, board: Board, buttonSize: Int) :
    Game(playerA, board, buttonSize) {

    val bot = bot
    override fun accessibleMove() {
        if (!board.justFindEnemy(
                if (currentPlayer == playerA.color) playerA else bot,
                if (currentPlayer == playerA.color) bot else playerA))
                {
                    currentPlayer = if(currentPlayer == playerA.color) bot.color else playerA.color
            Log.d(
                "GameOnePlayer",
                "${if (currentPlayer == playerA.color) playerA.color else bot.color} nie moze wykonac zadnego ruchu"
            )
        } else {
            Log.d(
                "GameOnePlayer",
                "${if (currentPlayer == playerA.color) playerA.color else bot.color}  moze wykonac ruchu"
            )
        }
    }

//    fun botMove()
//    {
//        val kordsBot = bot.botMove(game.board, game.playerA)
//        if (kordsBot[0] != -1) {
//            Log.d(
//                "Game1PlayerFragment",
//                "Bot ruszyl sie na i = ${kordsBot[1]} j =  ${kordsBot[2]}"
//            )
//            game.board.board[kordsBot[1]][kordsBot[2]].button.performClick()
//            if (game.isOver()) {
//                openGameOverDialog(view, game)
//            }
//        }
//    }

    override fun isOver(): Boolean {
        if ( emptyFields == 0
            || playerA.amountOfPawns == 0
            || bot.amountOfPawns == 0
        ) {
            Log.d("Game", "KONIEC GRY")
            return true
        }
        return false
    }
}