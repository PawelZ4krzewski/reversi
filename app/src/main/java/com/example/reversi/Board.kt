package com.example.reversi

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.util.Log
import android.view.animation.AlphaAnimation
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min

class Board(val boardSize: Int) {

    val pawnOnBoard = Array(8) { CharArray(boardSize) }

    val board = mutableListOf<MutableList<PawnButton>>()
    var currentColor: String = "B"

    init {
        repeat(boardSize)
        {
            board.add(mutableListOf<PawnButton>())
        }
    }


    fun move(currentP: String, i: Int, j: Int, currentPlayer: Player, enemy: Player): Boolean {
        this.currentColor = currentP
        var correctMove = false
        if (board[i][j].color == "X") {
            outfor1@ for (x in max(i - 1, 0) until min(i + 2, boardSize)) {
                for (y in max(j - 1, 0) until min(j + 2, boardSize)) {
                    if (board[x][y].color != "X") {
                        if (findEnemy(i, j, currentPlayer, enemy)) {
                            changePawnColor(i, j, true, currentPlayer, enemy)
                            correctMove = true
                            break@outfor1
                        }
                    }
                }
            }
        }
        return correctMove
    }

    fun justFindEnemy(currentPlayer: Player, enemy: Player): IntArray
    {
        Log.d(
            "board",
            "JUST FIND ENEMY"
        )
        for(i in 0 until boardSize)
        {
            for(j in 0 until boardSize)
            {
                if(board[i][j].color == "X")
                {
                    Log.d(
                        "board",
                        "JUST FIND ENEMY - i: $i j:$j color: ${board[i][j].color}"
                    )
                    if(findEnemy(i,j,currentPlayer,enemy,true)) {
                        return listOf(1,i,j).toIntArray()
                    }
                }
            }
        }
        return listOf(0).toIntArray()
    }
    private fun findEnemy(i: Int, j: Int, currentPlayer: Player, enemy: Player, justFind: Boolean = false): Boolean {
        var correctMove = false
        Log.d(
            "board",
            "findEnemy"
        )
        Log.d(
            "board",
            "i : $i j:$j, boardSize = $boardSize currentCOlor ${currentPlayer.color} enemyColor ${enemy.color}"
        )
        for (x in max(i - 1, 0) until min(i + 2, boardSize)) {
            for (y in max(j - 1, 0) until min(j + 2, boardSize)) {
                Log.d(
                    "board",
                    "petla dla $x $y pwnOnBoard = ${board[x][y].color}"
                )
                if (board[x][y].color == enemy.color && (x != i || y != j)) {
                    if (reverseColor(i, j, x - i, y - j, currentPlayer, enemy, justFind)) {
                        correctMove = true
                        Log.d("ReversiveBoard", "Mozna revesi zrobic")
                    } else {
                        Log.d("ReversiveBoard", "NIC NIE MOZNA")
                    }

                }
            }
        }
        return correctMove
    }

    private fun reverseColor(
        i: Int,
        j: Int,
        opi: Int,
        opj: Int,
        currentPlayer: Player,
        enemy: Player,
        justFind: Boolean
    ): Boolean {
        Log.d(
            "board",
            "reverseColor"
        )
        var ni: Int = i + opi
        var nj: Int = j + opj
        var flag = false



        Log.d("board", "i = $i j = $j opi =$opi opj = $opj ni = $ni nj = $nj")

        if (opi == 0) {
            while ((nj >= 0) && (nj != boardSize) && (board[ni][nj].color == enemy.color || board[ni][nj].color == currentPlayer.color)) {
                Log.d("board", "ni = $ni nj = $nj")
                if (board[ni][nj].color == currentPlayer.color) {
                    Log.d("board", "i = $i j = $j ni = $ni nj = $nj")
                    flag = true
                    break
                }
                nj += opj
            }
        }
        if (opj == 0) {
            while ((ni >= 0) && (ni != boardSize) && (board[ni][nj].color == enemy.color || board[ni][nj].color == currentPlayer.color)) {
                Log.d("board", "ni = $ni nj = $nj")
                if (board[ni][nj].color == currentPlayer.color) {
                    Log.d("board", "i = $i j = $j ni = $ni nj = $nj")
                    flag = true
                    break
                }
                ni += opi
            }
        } else {
            outWhile@ while ((ni >= 0) && (ni != boardSize) && (nj >= 0) && (nj != boardSize) && (board[ni][nj].color == enemy.color || board[ni][nj].color == currentPlayer.color)) {
                Log.d("board", "ni = $ni nj = $nj")
                if (board[ni][nj].color == currentPlayer.color) {
                    Log.d("board", "i = $i j = $j ni = $ni nj = $nj")
                    flag = true
                    break@outWhile
                }
                nj += opj
                ni += opi
            }
        }

        if(justFind) {
            return flag
        }
        if (flag) {
            var x = i + opi
            var y = j + opj
            while (x != ni || y != nj) {
                Log.d("board", "x = $x, y = $y")
                changePawnColor(x, y, false, currentPlayer, enemy)
                y += opj
                x += opi
            }
        }
        return flag
    }

    private fun changePawnColor(
        i: Int,
        j: Int,
        new: Boolean = true,
        currentPlayer: Player,
        enemy: Player
    ) {
        Log.d(
            "board",
            "changePawnColor"
        )
        Log.d(
            "board",
            "currentPlayerColor ${currentPlayer.color} currentColor $currentColor"
        )
        if (currentColor == currentPlayer.color) {

//            val b = .getResources().getDrawable(R.drawable.custom_progressbargreen);
//            val colors = arrayOf(getDrawable(,board[i][j].setColor(currentPlayer.color)),board[i][j].setColor(enemy.color))
//            val transition = TransitionDrawable(colors)
            board[i][j].button.animate().alpha(0.75f).setDuration(200).setListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    board[i][j].button.setImageResource(board[i][j].setColor(currentPlayer.color))
                    board[i][j].button.animate().alpha(1.0f).setDuration(500).setListener(null)
                }
            })
//            board[i][j].button.setImageDrawable(transition)
//            transition.startTransition(500)

            board[i][j].color=currentColor
            currentPlayer.amountOfPawns++
            if (!new) {
                enemy.amountOfPawns--
            }
        }

    }

}