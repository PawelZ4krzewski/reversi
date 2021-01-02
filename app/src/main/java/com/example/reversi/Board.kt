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
import android.widget.RelativeLayout
import androidx.core.view.marginStart
import kotlin.math.roundToInt
import kotlin.math.min
import kotlin.math.max
import androidx.core.view.marginBottom as marginBottom


class Board : Fragment() {

    val revBoard = ReversiBoard(8)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_board, container, false)

        val buttonSize = (if (revBoard.boardSize == 8) 45f else 60f).dpToPixels().roundToInt()
        val myLayout = view.findViewById<LinearLayout>(R.id.LinearLayout)


        val myButton = Button(requireContext())
        myButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        for (i in 0 until revBoard.boardSize) {

            val column = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
            }

            for (j in 0 until revBoard.boardSize) {

                revBoard.pawnOnBoard[i][j] = 'X'

                val button = Button(requireContext()).apply {
                    text = "X"
                    setBackgroundColor(Color.parseColor("#006200"))
                    layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
                    id = View.generateViewId()
                }

                if ((i == 3 && j == 3) || (i == 4 && j == 4)) {
                    button.text = "C"
                    revBoard.pawnOnBoard[i][j] = 'W'
                    button.setBackgroundColor(Color.parseColor("#ffffff"))
                }

                if ((i == 4 && j == 3) || (i == 3 && j == 4)) {
                    button.text = "B"
                    revBoard.pawnOnBoard[i][j] = 'B'
                    button.setBackgroundColor(Color.parseColor("#000000"))
                }
                column.addView(button)
                revBoard.board[i].add(button)
                revBoard.board[i][j].setOnClickListener()
                {
                    revBoard.move(i, j)
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


}