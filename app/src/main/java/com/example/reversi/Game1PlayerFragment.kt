package com.example.reversi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import java.io.File
import kotlin.math.log
import kotlin.math.roundToInt


class Game1PlayerFragment : Fragment() {

    private val itemViewModel: ItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game1_player, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var boardSize = 6
        val buttonSize = (45f).dpToPixels().roundToInt()
        var playerAColor = "B"
        var playerBColor = "W"

        val myFile = File(requireContext().filesDir,"gameSettings.txt")
        val isFile = myFile.createNewFile()
        if(isFile)
        {
            Log.d("SettingsFragment", "Utworzono nowy plik")
        }
        else
        {
            Log.d("SettingsFragment", "Plik juz istnieje")
        }

        val tekst = myFile.bufferedReader().readLines()
        for(x in tekst)
        {
            val settingLine = x.split(" ")

            when(settingLine[0])
            {
                "boardSize" -> {
                    boardSize = settingLine[1].toInt()
                }
                "playerAColor" -> {
                    playerAColor = settingLine[1]
                }
                "playerBColor" -> {
                    playerBColor = settingLine[1]
                }
            }
        }

        val board = Board(boardSize)
        val playerA = userPlayer(playerAColor, itemViewModel.player)
        val playerB = Bot(playerBColor, "PlayerB")
        val game = GameOnePlayer(playerA, playerB, board, buttonSize)
        game.currentPlayer = playerAColor
        createBoard(view, game)

        val resetButton = view.findViewById<Button>(R.id.resetButtonBot)
        resetButton.setOnClickListener()
        {
            view.findNavController().navigate(R.id.action_game1PlayerFragment_to_menu)
        }
    }

    private fun Float.dpToPixels(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }

    private fun createBoard(view: View, game: GameOnePlayer) {

        val myLayout = view.findViewById<LinearLayout>(R.id.LinearLayoutBot)



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

                val pawnButton = PawnButton(createDefaultButton(game), "X", i, j)

                if (i == (game.board.boardSize / 2 * 1.0).roundToInt() && j == (game.board.boardSize / 2 * 1.0).roundToInt() || (i == (game.board.boardSize / 2 * 1.0).roundToInt() - 1 && j == (game.board.boardSize / 2 * 1.0).roundToInt() - 1)) {
                    pawnButton.button.setImageResource(pawnButton.setColor(game.bot.color))
                }

                if ((i == (game.board.boardSize / 2 * 1.0).roundToInt() - 1 && j == (game.board.boardSize / 2 * 1.0).roundToInt()) || (i == (game.board.boardSize / 2 * 1.0).roundToInt() && j == (game.board.boardSize / 2 * 1.0).roundToInt() - 1)) {
                    pawnButton.button.setImageResource(pawnButton.setColor(game.playerA.color))
                }

                column.addView(pawnButton.button)
                game.board.board[i].add(pawnButton)
                game.board.board[i][j].button.setOnClickListener()
                {
                    if (!game.board.move(
                            game.currentPlayer,
                            i,
                            j,
                            if (game.currentPlayer == game.playerA.color) game.playerA else game.bot,
                            if (game.currentPlayer == game.playerA.color) game.bot else game.playerA
                        )
                    ) {
                        Toast.makeText(requireContext(), "Bledny ruch", Toast.LENGTH_SHORT).show()
                        Log.d("Game1PlayerFragment", "Bledny RUCH")
                    } else {
                        game.emptyFields--
                        game.currentPlayer =
                            if (game.currentPlayer == game.playerA.color) game.bot.color else game.playerA.color
                        game.accessibleMove()
                        updateScore(view, game)
                        if (game.isOver()) {
                            openGameOverDialog(view, game)
                        } else if (game.currentPlayer == game.bot.color) {
                            Log.d("Game1PlayerFragment", "Tura Bota")
                            val kordsBot = game.bot.botMove(game.board, game.playerA)
                            Log.d(
                                "Game1PlayerFragment",
                                "Bot ruszyl sie na i = ${kordsBot[1]} j =  ${kordsBot[2]}"
                            )
//                            Thread.sleep(200L)
                            game.board.board[kordsBot[1]][kordsBot[2]].button.performClick()
                        }
                    }

                }
            }
            myLayout.addView(column)
        }
        updateScore(view, game)
    }

    @SuppressLint("SetTextI18n")
    private fun openGameOverDialog(view: View, game: GameOnePlayer) {

        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(requireContext())
        val v = layoutInflater.inflate(R.layout.game_over, null)

        val winnerText = v.findViewById<TextView>(R.id.textViewWinner)
        Log.d("gameFragment", "${winnerText}")
        when {
            game.playerA.amountOfPawns > game.bot.amountOfPawns -> {
                winnerText.text = "${game.playerA.getName()} \n WINS!"
            }
            game.playerA.amountOfPawns < game.bot.amountOfPawns -> {
                winnerText.text = "${game.bot.getName()} \n WINS!"
            }
            else -> {
                winnerText.text = "REMIS"
            }
        }
        val resetButton = v.findViewById<Button>(R.id.buttonRetryDialog)
        resetButton.setOnClickListener() {
            Log.d("gameFragment", "NIE DZIALA")
        }

        val buttonExit = v.findViewById<Button>(R.id.buttonExitDialog)
        buttonExit.setOnClickListener()
        {
            Log.d("gameFragment", "EXIT BUTTON")
            view.findNavController().navigate(R.id.action_game1PlayerFragment_to_menu)
            dialog?.dismiss()
        }

        builder.setView(v)
        dialog = builder.create()
        dialog.show()
    }


    private fun createDefaultButton(game: GameOnePlayer): ImageButton {
        return ImageButton(requireContext()).apply {
            setBackgroundResource(R.drawable.board_button_background)
            layoutParams = LinearLayout.LayoutParams(game.buttonSize, game.buttonSize)
                .apply { setMargins(3,3, 3, 3) }
            id = View.generateViewId()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateScore(view: View, game: GameOnePlayer) {
        view.findViewById<TextView>(R.id.textViewPlayerA).text =
            "${game.playerA.getName()}  \n ${game.playerA.amountOfPawns}"
        view.findViewById<TextView>(R.id.textViewBot).text =
            "${game.bot.getName()}  \n ${game.bot.amountOfPawns}"
        view.findViewById<TextView>(R.id.currentPlayerBot).text = "${game.currentPlayer}"
    }
}