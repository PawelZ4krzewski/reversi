package com.example.reversi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
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
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.game_over.*
import kotlin.math.roundToInt


class gameFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val boardSize = 4
        val buttonSize = (45f).dpToPixels().roundToInt()
        val board = Board(boardSize)
        val playerA = Player('B', "PlayerA")
        val playerB = Player('W', "PlayerB")
        val game = Game(playerA, playerB, board, buttonSize)

        createBoard(view, game)
        updateScore(view, game)

        val resetButton = view.findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener()
        {
            view.findNavController().navigate(R.id.action_gameFragment_to_menu)
        }
    }

    private fun Float.dpToPixels(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }

    private fun createBoard(view: View, game: Game) {

        val myLayout = view.findViewById<LinearLayout>(R.id.LinearLayout)


        for (i in 0 until game.board.boardSize) {

            val column = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
            }

            for (j in 0 until game.board.boardSize) {

                game.board.pawnOnBoard[i][j] = 'X'

                val pawnButton = PawnButton(createDefaultButton(game), 'X', i, j)

                if (i == (game.board.boardSize/2 * 1.0).roundToInt() && j == (game.board.boardSize/2 * 1.0).roundToInt() || (i == (game.board.boardSize/2 * 1.0).roundToInt() - 1  && j == (game.board.boardSize/2 * 1.0).roundToInt() - 1)) {
                    pawnButton.button.setBackgroundColor(Color.parseColor(pawnButton.setColor(game.playerB.color)))
                }

                if ((i == (game.board.boardSize/2 * 1.0).roundToInt() - 1 && j == (game.board.boardSize/2 * 1.0).roundToInt()) || (i == (game.board.boardSize/2 * 1.0).roundToInt() && j == (game.board.boardSize/2 * 1.0).roundToInt() - 1)) {
                    pawnButton.button.setBackgroundColor(Color.parseColor(pawnButton.setColor(game.playerA.color)))
                }

                column.addView(pawnButton.button)
                game.board.board[i].add(pawnButton)
                game.board.board[i][j].button.setOnClickListener()
                {
                    if (!game.board.move(
                            game.currentPlayer,
                            i,
                            j,
                            if (game.currentPlayer == game.playerA.color) game.playerA else game.playerB,
                            if (game.currentPlayer == game.playerA.color) game.playerB else game.playerA
                        )
                    ) {
                        Toast.makeText(requireContext(), "Bledny ruch", Toast.LENGTH_SHORT).show()
                        Log.d("Board", "Bledny RUCH")
                    } else {
                        game.emptyFields--
                        game.currentPlayer =
                            if (game.currentPlayer == game.playerA.color) game.playerB.color else game.playerA.color
                        game.accessibleMove()
                        updateScore(view, game)
                        if(game.isOver())
                        {
                            openGameOverDialog(view, game)
                        }
                    }

                }
            }
            //Log.d("MainActivity", "i = $i j = $j ${pawnOnBoard[i][j].toString()}")
            myLayout.addView(column)
        }
    }

    private fun openGameOverDialog(view: View, game: Game) {

        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(requireContext())
        val v = layoutInflater.inflate(R.layout.game_over, null)

        val winnerText = v.findViewById<TextView>(R.id.textViewWinner)
        Log.d("gameFragment","${winnerText}")
            when {
                game.playerA.amountOfPawns > game.playerB.amountOfPawns -> {
                    winnerText.text = "${game.playerA.name} \n WINS!"
                }
                game.playerA.amountOfPawns < game.playerB.amountOfPawns -> {
                    winnerText.text = "${game.playerB.name} \n WINS!"
                }
                else -> {
                    winnerText.text = "REMIS"
                }
            }
            val resetButton = v.findViewById<Button>(R.id.buttonRetryDialog)
//            Log.d("gameFragment","${resetButton}")
            resetButton.setOnClickListener() {
                Log.d("gameFragment","EXIT BUTTON")
                TODO()
                dialog?.dismiss()

            }

            val buttonExit = v.findViewById<Button>(R.id.buttonExitDialog)
            buttonExit.setOnClickListener()
            {
                Log.d("gameFragment","EXIT BUTTON")
                view.findNavController().navigate(R.id.action_gameFragment_to_menu)
                dialog?.dismiss()
            }

        builder.setView(v)
        dialog = builder.create()
        dialog.show()
    }


    private fun createDefaultButton(game: Game): Button {
        return Button(requireContext()).apply {
            setBackgroundColor(Color.parseColor("#006200"))
            layoutParams = LinearLayout.LayoutParams(game.buttonSize, game.buttonSize)
                .apply { setMargins(5, 5, 5, 5) }
            id = View.generateViewId()
        }
    }

    private fun updateScore(view: View, game: Game) {
        view.findViewById<TextView>(R.id.textViewA).text =
            "${game.playerA.color}  \n ${game.playerA.amountOfPawns}"
        view.findViewById<TextView>(R.id.textViewB).text =
            "${game.playerB.color}  \n ${game.playerB.amountOfPawns}"
        view.findViewById<TextView>(R.id.currentPlayer).text = "${game.currentPlayer}"
    }
}