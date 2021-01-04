package com.example.reversi

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt


class Board : Fragment() {

    val revBoard = ReversiBoard(8)
    val game    = Game(2)
    val playerA = Player('B',"PlayerA")
    val playerB = Player('W',"PlayerB")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createBoard(view)
        game.addScore(view.findViewById<TextView>(R.id.textViewA))
        game.addScore(view.findViewById<TextView>(R.id.textViewB))
        game.addScore(view.findViewById<TextView>(R.id.currentPlayer))
        val resetButton = view.findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener()
        {
            Log.d("Board","Resetujemy")
        }
        updateScore()
    }

    private fun Float.dpToPixels(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }

    @Suppress("NAME_SHADOWING")
    private fun createBoard(view:View)
    {
        val buttonSize = (if (revBoard.boardSize == 8) 45f else 60f).dpToPixels().roundToInt()
        val myLayout = view.findViewById<LinearLayout>(R.id.LinearLayout)


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
                    layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize).apply { setMargins(5,5,5,5) }
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
                    if(!revBoard.move(i, j))
                    {
                        Toast.makeText(requireContext(),"Bledny ruch",Toast.LENGTH_SHORT).show()
                        Log.d("Board", "Bledny RUCH")
                    }
                    else
                    {
                        updateScore()
                        if(game.isOver(revBoard.boardSize))
                        {
                            Log.d("Board","---------KONIEC GRY----------")
                            if (game.score[0]>game.score[1])
                            {
                                Log.d("Board","Wygral gracz 1")
                            }
                            else if(game.score[0]<game.score[1])
                            {
                                Log.d("Board","Wygral gracz 2")
                            }
                            else
                            {
                                Log.d("Board","Mamy remis")
                            }
                        }
                    }

                }
            }
            //Log.d("MainActivity", "i = $i j = $j ${pawnOnBoard[i][j].toString()}")
            myLayout.addView(column)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateScore(playerA : String = "Player One", playerB : String = "Player Two")
    {
        game.scoreTextView[0].text = "$playerA  \n ${revBoard.amountOfBlackPawns}"
        game.scoreTextView[1].text = "$playerB  \n ${revBoard.amountOfWhitePawns}"
        game.scoreTextView[2].text = "${revBoard.currentPlayer}"
        game.score[0] = revBoard.amountOfBlackPawns
        game.score[1] = revBoard.amountOfWhitePawns
    }

}