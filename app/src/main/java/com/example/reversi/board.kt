package com.example.reversi

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import kotlin.math.roundToInt
import kotlin.math.min
import kotlin.math.max


class board : Fragment() {

    var boardSize = 8
    val pawnOnBoard = Array(8) { CharArray(boardSize) }
    val board = mutableListOf<MutableList<Button>>()
    var currentPlayer: Char = 'B'

    init {
        repeat(boardSize)
        {
            board.add(mutableListOf<Button>())
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_board, container, false)


        val buttonSize = (if (boardSize == 8) 45f else 60f).dpToPixels().roundToInt()
        val myLayout = view.findViewById<LinearLayout>(R.id.LinearLayout)


        val myButton = Button(requireContext())
        myButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        for (i in 0 until boardSize) {

            val column = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
            }

            for (j in 0 until boardSize) {

                pawnOnBoard[i][j] = 'X'

                val button = Button(requireContext()).apply {
                    text = "X"
                    setBackgroundColor(Color.parseColor("#006200"))
                    layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
                    id = View.generateViewId()
                }

                if ((i == 3 && j == 3) || (i == 4 && j == 4)) {
                    button.text = "C"
                    pawnOnBoard[i][j] = 'W'
                    button.setBackgroundColor(Color.parseColor("#ffffff"))
                }

                if ((i == 4 && j == 3) || (i == 3 && j == 4)) {
                    button.text = "B"
                    pawnOnBoard[i][j] = 'B'
                    button.setBackgroundColor(Color.parseColor("#000000"))
                }
                column.addView(button)
                board[i].add(button)
                board[i][j].setOnClickListener()
                {
                    move(i, j)
                }
            }
            //Log.d("MainActivity", "i = $i j = $j ${pawnOnBoard[i][j].toString()}")
            myLayout.addView(column)

        }

        return view
    }

    private fun Float.dpToPixels(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }

    private fun move(i: Int, j: Int) {
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