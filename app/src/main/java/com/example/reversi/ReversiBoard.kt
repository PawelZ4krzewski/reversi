package com.example.reversi

import android.graphics.Color
import android.util.Log
import android.widget.Button
import kotlin.math.max
import kotlin.math.min

class ReversiBoard(val boardSize: Int) {
    val pawnOnBoard = Array(8) { CharArray(boardSize) }
    val board = mutableListOf<MutableList<Button>>()
    var currentPlayer: Char = 'B'
    init {
        repeat(boardSize)
        {
            board.add(mutableListOf<Button>())
        }
    }


    fun move(i: Int, j: Int) {
        if (pawnOnBoard[i][j] == 'X') {
            outfor1@ for (x in max(i - 1, 0) until min(i + 2, boardSize)) {
                for (y in max(j - 1, 0) until min(j + 2, boardSize)) {
                    if (pawnOnBoard[x][y] != 'X') {
                        findEnemy(i, j)
                        changePawnColor(i,j)
                        currentPlayer = if (currentPlayer == 'W') 'B' else 'W'
                        break@outfor1
                    }
                }
            }
        }
    }

    private fun findEnemy(i: Int, j: Int) {
        val enemy = if (currentPlayer == 'W') 'B' else 'W'
        Log.d(
            "board",
            "findEnemy"
        )
        for (x in max(i - 1, 0) until min(i + 2, boardSize)) {
            for (y in max(j - 1, 0) until min(j + 2, boardSize)) {

                if (pawnOnBoard[x][y] == enemy && (x != i || y != j)) {
                    Log.d(
                        "board",
                        "Obecny pionek $currentPlayer przeciwnik $enemy Znaleziono przeciwnika na $x $y"
                    )
                    reverseColor(i,j,x-i,y-j)
                }
            }
        }

    }

    private fun reverseColor(i: Int, j: Int, opi: Int, opj: Int) {
        Log.d(
            "board",
            "reverseColor"
        )
        val enemy = if (currentPlayer == 'W') 'B' else 'W'
        var ni: Int = i + opi
        var nj: Int = j + opj
        var flag = false
        Log.d("board", "i = $i j = $j opi =$opi opj = $opj ni = $ni nj = $nj")

        if(opi == 0) {
            while ((nj >= 0) && (nj != boardSize) && (pawnOnBoard[ni][nj] == enemy || pawnOnBoard[ni][nj] == currentPlayer)) {
                Log.d("board", "ni = $ni nj = $nj")
                if (pawnOnBoard[ni][nj] == currentPlayer) {
                    Log.d("board", "i = $i j = $j ni = $ni nj = $nj")
                    flag = true
                    break
                }
                nj += opj
            }
        }
        if(opj == 0) {
            while ((ni >= 0) && (ni != boardSize) && (pawnOnBoard[ni][nj] == enemy || pawnOnBoard[ni][nj] == currentPlayer)) {
                Log.d("board", "ni = $ni nj = $nj")
                if (pawnOnBoard[ni][nj] == currentPlayer) {
                    Log.d("board", "i = $i j = $j ni = $ni nj = $nj")
                    flag = true
                    break
                }
                ni += opi
            }
        }
        else
        {
            outWhile@ while ((ni >= 0) && (ni != boardSize) && (nj >= 0) && (nj != boardSize) && (pawnOnBoard[ni][nj] == enemy || pawnOnBoard[ni][nj] == currentPlayer)) {
                Log.d("board", "ni = $ni nj = $nj")
                if (pawnOnBoard[ni][nj] == currentPlayer) {
                    Log.d("board", "i = $i j = $j ni = $ni nj = $nj")
                    flag = true
                    break@outWhile
                }
                nj += opj
                ni += opi
            }
        }

        if (flag) {
            if(opi == 0)
            {
                for (y in min(nj, j) + 1 until max(nj, j)) {
                    Log.d("board","x = $i, y = $y")
                    changePawnColor(i, y)
                }
            }
            else if(opj == 0)
            {
                for (x in min(ni, i) + 1 until max(ni, i)) {
                    Log.d("board","x = $x, y = $j")
                    changePawnColor(x, j)
                }
            }
            else
            {
                var x = i+opi
                var y = j+opj
                while( x != ni || y != nj) {
                    Log.d("board", "x = $x, y = $y")
                    changePawnColor(x, y)
                    y += opj
                    x += opi
                }
            }
        }
    }

    private fun changePawnColor(i: Int, j: Int) {
        Log.d(
            "board",
            "changePawnColor"
        )
        if (currentPlayer == 'W') {
            board[i][j].setBackgroundColor(Color.parseColor("#ffffff"))
            pawnOnBoard[i][j] = 'W'
        } else {
            board[i][j].setBackgroundColor(Color.parseColor("#000000"))
            pawnOnBoard[i][j] = 'B'
        }
    }

}