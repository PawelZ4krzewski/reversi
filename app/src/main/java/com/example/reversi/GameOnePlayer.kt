package com.example.reversi

import android.util.Log

class GameOnePlayer(playerA: userPlayer, bot: Bot, board: Board, buttonSize: Int) : Game(playerA, board, buttonSize) {

    val bot = bot
    override fun accessibleMove(){
        val pom = board.justFindEnemy(
            if (currentPlayer == playerA.color) playerA else bot,
            if (currentPlayer == playerA.color) bot else playerA)
        if (pom[0]==0) {
            currentPlayer =
                if (currentPlayer == playerA.color) bot.color else playerA.color
            Log.d("Game", "${if (currentPlayer == playerA.color) playerA.color else bot.color} nie moze wykonac zadnego ruchu")
        } else {
            Log.d(
                "Game",
                "${if (currentPlayer == playerA.color) playerA.color else bot.color}  moze wykonac ruchu na i: ${pom[1]}, j: ${pom[2]}"
            )
        }
    }
    override fun isOver() : Boolean
    {
        if((board.justFindEnemy(this.playerA, bot)[0]==0 && board.justFindEnemy(bot, playerA)[0]==0) || bot.amountOfPawns+playerA.amountOfPawns == board.boardSize*board.boardSize)
        {
            Log.d("Game","KONIEC GRY")
            return true
        }
        return false
    }
}