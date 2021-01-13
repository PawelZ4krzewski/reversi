package com.example.reversi

import android.util.Log
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Bot(color: String, name: String): Player(color, name) {



    fun botMove(board: Board, enemy: Player): IntArray
    {
        var pomArray = IntArray(3)
        var p = 0
        var sizeOfMutableList = 0
        val listOfAvailableMove = mutableListOf<MutableList<Int>>() // i j 1
        for(i in 0 until board.boardSize)
        {
            for(j in 0 until board.boardSize)
            {
                if(board.board[i][j].color == "X")
                {

                    pomArray = findAvailableMove(i,j,enemy,board)

                    if(pomArray[3] == 1)
                    {
                        listOfAvailableMove.add(mutableListOf<Int>())
                        listOfAvailableMove[p].add(pomArray[0])
                        listOfAvailableMove[p].add(pomArray[1])
                        listOfAvailableMove[p++].add(pomArray[2])
                        sizeOfMutableList++
                    }
                }
            }
        }

        return listOfAvailableMove[(0 until sizeOfMutableList).random()].toIntArray()
    }


    private fun findAvailableMove(i: Int, j: Int,enemy: Player, gameBoard: Board): IntArray  {
        //MutableList<MutableList<Int>>
        var tabPom = IntArray(2)
        val bestOption = IntArray(4)
        bestOption[0] = 0 // the best available move (in score)
        bestOption[3] = 0 //don't find available move


        Log.d(
            "board",
            "findEnemy"
        )
        Log.d(
            "board",
            "i : $i j:$j, boardSize = ${gameBoard.boardSize} currentCOlor ${this.color} enemyColor ${enemy.color}"
        )
        for (x in max(i - 1, 0) until min(i + 2, gameBoard.boardSize)) {
            for (y in max(j - 1, 0) until min(j + 2, gameBoard.boardSize)) {
                Log.d(
                    "board",
                    "petla dla $x $y pwnOnBoard = ${gameBoard.board[x][y].color}"
                )
                if (gameBoard.board[x][y].color == enemy.color && (x != i || y != j)) {
                    tabPom = calculateAvailableScore(i, j, x - i, y - j, enemy, gameBoard)
                    if (tabPom[0] == 1) {
                        if(tabPom[1]>bestOption[0])
                        {
                            bestOption[0]=tabPom[1]
                            bestOption[1] = i
                            bestOption[2] = j
                            bestOption[3] = 1
                        }
//                        listOfAvailableMove.add(mutableListOf<Int>())
//                        listOfAvailableMove[p].add(tabPom[1])
//                        listOfAvailableMove[p].add(i)
//                        listOfAvailableMove[p++].add(j)
                        Log.d("ReversiveBoard", "Mozna revesi zrobic")
                    } else {
                        Log.d("ReversiveBoard", "NIC NIE MOZNA")
                    }
                }
            }
        }
        return bestOption
    }

    private fun calculateAvailableScore(
        i: Int,
        j: Int,
        opi: Int,
        opj: Int,
        enemy: Player,
        gameBoard: Board
    ): IntArray {
        Log.d(
            "board",
            "reverseColor"
        )
        var ni: Int = i + opi
        var nj: Int = j + opj
        var flag: Int = 0

        Log.d("board", "i = $i j = $j opi =$opi opj = $opj ni = $ni nj = $nj")

        if (opi == 0) {
            while ((nj >= 0) && (nj != gameBoard.boardSize) && (gameBoard.board[ni][nj].color == enemy.color || gameBoard.board[ni][nj].color == this.color)) {
                Log.d("board", "ni = $ni nj = $nj")
                if (gameBoard.board[ni][nj].color == this.color) {
                    Log.d("board", "i = $i j = $j ni = $ni nj = $nj")
                    flag = 1
                    break
                }
                nj += opj
            }
        }
        if (opj == 0) {
            while ((ni >= 0) && (ni != gameBoard.boardSize) && (gameBoard.board[ni][nj].color == enemy.color || gameBoard.board[ni][nj].color == this.color)) {
                Log.d("board", "ni = $ni nj = $nj")
                if (gameBoard.board[ni][nj].color == this.color) {
                    Log.d("board", "i = $i j = $j ni = $ni nj = $nj")
                    flag = 1
                    break
                }
                ni += opi
            }
        } else {
            outWhile@ while ((ni >= 0) && (ni != gameBoard.boardSize) && (nj >= 0) && (nj != gameBoard.boardSize) && (gameBoard.board[ni][nj].color == enemy.color || gameBoard.board[ni][nj].color == this.color)) {
                Log.d("board", "ni = $ni nj = $nj")
                if (gameBoard.board[ni][nj].color == this.color) {
                    Log.d("board", "i = $i j = $j ni = $ni nj = $nj")
                    flag = 1
                    break@outWhile
                }
                nj += opj
                ni += opi
            }
        }

        val maks = max(abs(i-ni), abs(j-nj))
        return intArrayOf(flag,maks)
    }
}