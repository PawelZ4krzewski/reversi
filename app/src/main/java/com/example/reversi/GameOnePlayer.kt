package com.example.reversi

import android.util.Log

class GameOnePlayer(playerA: userPlayer, bot: Bot, board: Board, buttonSize: Int) : Game(playerA, board, buttonSize) {

    val bot = bot
    override fun accessibleMove(){
        if (!board.justFindEnemy(
                if (currentPlayer == playerA.color) playerA else bot,
                if (currentPlayer == playerA.color) bot else playerA
            )
        ) {
            currentPlayer =
                if (currentPlayer == playerA.color) bot.color else playerA.color
            Log.d("Game", "${if (currentPlayer == playerA.color) playerA.color else bot.color} nie moze wykonac zadnego ruchu")
        } else {
            Log.d(
                "Game",
                "${if (currentPlayer == playerA.color) playerA.color else bot.color}  moze wykonac ruchu"
            )
        }
    }
    override fun isOver() : Boolean
    {
        if((!board.justFindEnemy(this.playerA, bot) && !board.justFindEnemy(bot, playerA)) || emptyFields == 0)
        {
            Log.d("Game","KONIEC GRY")
            return true
        }
        return false
    }
}