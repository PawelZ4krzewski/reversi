package com.example.reversi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import java.io.File
import kotlin.concurrent.thread
import kotlin.math.roundToInt


class Game2PlayerFragment : Fragment() {

    private var timerHandler: Handler? = null
    private val itemViewModel: ItemViewModel by activityViewModels()
    private var timePassed = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game2_player, container, false)
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
        val playerB = userPlayer(playerBColor, "PlayerB")
        val game = GameTwoPlayers(playerA, playerB, board, buttonSize)
        askAboutPlayer(view, game)
        createBoard(view, game)

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

    private fun createBoard(view: View, game: GameTwoPlayers) {

        val myLayout = view.findViewById<LinearLayout>(R.id.LinearLayout)
        updateScore(view,game)

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
                    pawnButton.button.setImageResource(pawnButton.setColor(game.playerB.color))
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
            myLayout.addView(column)
        }
        updateScore(view, game)
    }

    @SuppressLint("SetTextI18n")
    private fun openGameOverDialog(view: View, game: GameTwoPlayers) {

        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(requireContext())
        val v = layoutInflater.inflate(R.layout.game_over, null)

        val winnerText = v.findViewById<TextView>(R.id.textViewWinner)
        Log.d("gameFragment","${winnerText}")
            when {
                game.playerA.amountOfPawns > game.playerB.amountOfPawns -> {
                    saveRanking(2,game.playerA.getName())
                    saveRanking(-1,game.playerB.getName())
                    winnerText.text = "${game.playerA.getName()}\nWINS!"
                }
                game.playerA.amountOfPawns < game.playerB.amountOfPawns -> {
                    saveRanking(2,game.playerB.getName())
                    saveRanking(-1,game.playerA.getName())
                    winnerText.text = "${game.playerB.getName()}\nWINS!"
                }
                else -> {
                    saveRanking(1,game.playerA.getName())
                    saveRanking(1,game.playerB.getName())
                    winnerText.text = "REMIS"
                }
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

    private fun saveRanking(score: Int, name: String)
    {
        thread{
            val myFile = File(requireContext().filesDir, "ranking.txt")
            val isFile = myFile.createNewFile()
            if (isFile) {
                Log.d("SettingsFragment", "Utworzono nowy plik")
            } else {
                Log.d("SettingsFragment", "Plik juz istnieje")
            }

            Log.d("zapisRankingu", "CHce dac graczowi ${name} punktow ${score}")
            val tekst = myFile.bufferedReader().readLines()
            var i = 0
            var isPlayer = true
            for (x in tekst) {
                val settingLine = x.split(";")
                Log.d("zapisRankingu", "odczytuje ${settingLine[0]} z ${settingLine[1]}")
                if (settingLine[0] == name) {
                    isPlayer = false
                    if (i == 0) {
                        Log.d(
                            "zapisRankingu",
                            "Tworze na poczatku ${name} z ${settingLine[1].toInt() + score}"
                        )
                        myFile.writeText("${settingLine[0]};${settingLine[1].toInt() + score}\n")
                    } else {
                        Log.d("zapisRankingu", "dodaje ${name} z ${settingLine[1].toInt() + score}")
                        myFile.appendText("${settingLine[0]};${settingLine[1].toInt() + score}\n")
                    }
                } else {
                    if (i == 0) {
                        Log.d(
                            "zapisRankingu",
                            "Przepisuje na poczatek ${name} z ${settingLine[1].toInt()}"
                        )
                        myFile.writeText("${settingLine[0]};${settingLine[1].toInt()}\n")
                    } else {
                        Log.d("zapisRankingu", "Przepisuje ${name} z ${settingLine[1].toInt()}")
                        myFile.appendText("${settingLine[0]};${settingLine[1].toInt()}\n")
                    }
                }
                i++
            }

            if (isPlayer) {
                Log.d("zapisRankingu", "Dodaje nowego gracza o nazwie ${name}")
                myFile.appendText("$name;${score}\n")
            }
        }
    }

    private fun askAboutPlayer(view: View, game: GameTwoPlayers){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(requireContext())
        val v = layoutInflater.inflate(R.layout.ask_name_of_player, null)

        v.findViewById<Button>(R.id.buttonSendPlayer2Name).setOnClickListener(){
            game.playerB.setName(v.findViewById<EditText>(R.id.editTextPlayer2Name).text.toString())
            updateScore(view,game)
            clock(view)
            dialog?.dismiss()
        }

        builder.setView(v)
        dialog = builder.create()
        dialog.show()
    }

    private fun createDefaultButton(game: GameTwoPlayers): ImageButton {
        return ImageButton(requireContext()).apply {
            setBackgroundResource(R.drawable.board_button_background)
            layoutParams = LinearLayout.LayoutParams(game.buttonSize, game.buttonSize)
                .apply { setMargins(5, 5, 5, 5) }
            id = View.generateViewId()
        }
    }

    private fun clock(view: View)
    {
        timerHandler = Handler()
        timerHandler?.postDelayed(object : Runnable {
            override fun run() {
                val minutes = (timePassed / 60).toString().padStart(2, '0')
                val seconds = (timePassed % 60).toString().padStart(2, '0')
                view.findViewById<TextView>(R.id.time2Players).text = "$minutes:$seconds"
                timerHandler?.postDelayed(this, 1000)
                timePassed++
            }
        }, 1000)
    }

    private fun updateScore(view: View, game: GameTwoPlayers) {
        view.findViewById<TextView>(R.id.textViewA).text =
            "${game.playerA.getName()}\n${game.playerA.amountOfPawns}"
        view.findViewById<TextView>(R.id.textViewB).text =
            "${game.playerB.getName()}\n${game.playerB.amountOfPawns}"

        val pomButton = view.findViewById<ImageButton>(R.id.buttonCurrentPlayer2Players)
        val currentPlayerColor = if (game.currentPlayer == game.playerA.color) game.playerA.color else game.playerB.color
        val currentPlayerPawnButton = PawnButton(pomButton, currentPlayerColor,-1,-1 )
        pomButton.setImageResource(currentPlayerPawnButton.setColor(currentPlayerColor))
    }
}