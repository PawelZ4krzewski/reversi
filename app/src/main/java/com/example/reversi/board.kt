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
                    text = (if ((i + j).rem(2) == 0) "X" else "O")
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
        if (currentPlayer == 'B' && pawnOnBoard[i][j] == 'X') {
            outfor1@for (x in max(i - 1,0) until min(i + 2,boardSize)) {
                for (y in max(j - 1, 0) until min(j + 2, boardSize)) {
                    if (pawnOnBoard[x][y] != 'X') {
                        changePawnColor(i,j)
                        break@outfor1
                    }
                }
            }
        }
        else if (currentPlayer == 'W' && pawnOnBoard[i][j] == 'X') {
            outfor@for (x in max(i - 1, 0) until min(i+2,boardSize))
            {
                for (y in max(j - 1, 0) until min(j+2,boardSize))
                {
                    if(pawnOnBoard[x][y] != 'X')
                    {
                        changePawnColor(i,j)
                        break@outfor
                    }
                }
            }
        } else {
            Log.d("MainActivity", "Nieprawidlowy ruch")
        }
    }

    private fun findEnemy(i:Int, j:Int)
    {
        val enemy = if(currentPlayer == 'W') 'B' else 'W'

            for (x in max(i - 1, 0) until min(i+2,boardSize))
            {
                for (y in max(j - 1, 0) until min(j+2,boardSize))
                {
                    if(pawnOnBoard[x][y] == enemy)
                    {
                        reverseColor(i,j,x-i,y-j)
                    }
                }
            }

    }

    private fun reverseColor(i:Int, j:Int, opi: Int, opj: Int )
    {
        val enemy = if(currentPlayer == 'W') 'B' else 'W'
        var ni: Int = i + opi
        var nj: Int  = j + opj
        var ki = 0
        var kj = 0
        var flag = false
        outWhile@while((ni!=0) || (ni!=boardSize-1) && (pawnOnBoard[ni][nj] == enemy || pawnOnBoard[ni][nj] == currentPlayer))
        {
            while((nj!=0) || (nj!=boardSize-1) && (pawnOnBoard[ni][nj] == enemy || pawnOnBoard[ni][nj] == currentPlayer))
            {
                if(pawnOnBoard[ni][nj] == currentPlayer)
                {
                    flag = true
                    ki = ni
                    kj = nj

                }
            }
            ni += opi
            nj += opj
        }

        if(flag) {
            for (x in min(ki, i) until max(ki, i)) {
                for (y in min(kj, j) until max(kj, j)) {
                    changePawnColor(x,y);
                }
            }
        }
    }

    private fun changePawnColor(i:Int, j:Int)
    {
        if(pawnOnBoard[i][j] == 'X')
        {
          if(currentPlayer == 'W')
          {
              board[i][j].setBackgroundColor(Color.parseColor("#ffffff"))
              currentPlayer = 'B'
              pawnOnBoard[i][j] = 'W'          }
          else
          {
              board[i][j].setBackgroundColor(Color.parseColor("#000000"))
              currentPlayer = 'W'
              pawnOnBoard[i][j] = 'B'
          }
        }
        findEnemy(i,j)
    }
}