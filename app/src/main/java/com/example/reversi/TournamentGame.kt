package com.example.reversi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import java.io.File
import kotlin.math.roundToInt


class TournamentGame : Fragment() {

    private var timerHandler: Handler? = null
    private var timePassed = 1
    val args: TournamentGameArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tournament_game, container, false)
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
        val linesTournament = readTournamentInformation()


        val playerAName  = linesTournament[2+linesTournament[1].toInt()+linesTournament[0].toInt()].split(" vs ")[0]
        val playerBName  = linesTournament[2+linesTournament[1].toInt()+linesTournament[0].toInt()].split(" vs ")[1]
//        val playerAName  = "PlayerA"
//        val playerBName  = "PlayerB"

        val board = Board(boardSize)
        val playerA = userPlayer(playerAColor, playerAName)
        val playerB = userPlayer(playerBColor, playerBName)
        val game = GameTwoPlayers(playerA, playerB, board, buttonSize)
        createBoard(view, game)

    }

    private fun Float.dpToPixels(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }

    private fun createBoard(view: View, game: GameTwoPlayers) {

        clock(view)

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
                saveTournamentInformation(2,0)
                winnerText.text = "${game.playerA.getName()} \n WINS!"
            }
            game.playerA.amountOfPawns < game.playerB.amountOfPawns -> {
                saveTournamentInformation(0,2)
                winnerText.text = "${game.playerB.getName()} \n WINS!"
            }
            else -> {
                saveTournamentInformation(1,1)
                winnerText.text = "REMIS"
            }
        }

        val buttonExit = v.findViewById<Button>(R.id.buttonExitDialog)
        buttonExit.setOnClickListener()
        {
            Log.d("gameFragment","EXIT BUTTON")
            TournamentGameDirections.actionTournamentGame2ToTournamentInformation(args.tournamentNameGame)
            view.findNavController().navigate(TournamentGameDirections.actionTournamentGame2ToTournamentInformation(args.tournamentNameGame))
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
                view.findViewById<TextView>(R.id.timeTournament).text = "$minutes:$seconds"
                timerHandler?.postDelayed(this, 1000)
                timePassed++
            }
        }, 1000)
    }

    private fun updateScore(view: View, game: GameTwoPlayers) {

        view.findViewById<TextView>(R.id.textViewPlayerATournament).text =
            "${game.playerA.getName()}\n${game.playerA.amountOfPawns}"
        view.findViewById<TextView>(R.id.textViewPlayerBTournament).text =
            "${game.playerB.getName()}\n${game.playerB.amountOfPawns}"

        val pomButton = view.findViewById<ImageButton>(R.id.buttonCurrentPlayerTournament)
        val currentPlayerColor = if (game.currentPlayer == game.playerA.color) game.playerA.color else game.playerB.color
        val currentPlayerPawnButton = PawnButton(pomButton, currentPlayerColor,-1,-1 )
        pomButton.setImageResource(currentPlayerPawnButton.setColor(currentPlayerColor))
    }

    private fun readTournamentInformation() : List<String>
    {
        val tournamentName = args.tournamentNameGame
        Log.d("TournamentFragment", "TOURNAMENT NAME $tournamentName.txt")
        val myFile = File(requireContext().filesDir,"$tournamentName.txt")
        val isFile = myFile.createNewFile()
        if(isFile)
        {
            Log.d("TournamentFragment", "Utworzono nowy plik o nazwie $tournamentName.txt")
        }
        else
        {
            Log.d("TournamentFragment", "Plik o nazwie $tournamentName.txt juz istnieje")
        }

        val lines = myFile.bufferedReader().readLines()

        return lines
    }

    private fun saveTournamentInformation(aPoint: Int, bPoint: Int)
    {
        val tournamentName = args.tournamentNameGame
        Log.d("TournamentFragment", "TOURNAMENT NAME $tournamentName.txt")
        val myFile = File(requireContext().filesDir,"$tournamentName.txt")
        val isFile = myFile.createNewFile()
        if(isFile)
        {
            Log.d("TournamentFragment", "Utworzono nowy plik o nazwie $tournamentName.txt")
        }
        else
        {
            Log.d("TournamentFragment", "Plik o nazwie $tournamentName.txt juz istnieje")
        }

        val lines = myFile.bufferedReader().readLines()
        val playerA  = lines[2+lines[1].toInt()+lines[0].toInt()].split(" vs ")[0]
        val playerB  = lines[2+lines[1].toInt()+lines[0].toInt()].split(" vs ")[1]

        Log.d("TournamentFragment", "CO JEST W PLIKU!!!!! PRZED ZAPISEM")
        for(x in lines)
        {
            Log.d("TournamentFragment", x)
        }

        for(x in 0 until lines.size) {
            Log.d("TournamentFragment",lines[x])
            if(x == 0){
                myFile.writeText("${lines[0]}\n")
                Log.d("TournamentFragment","na $x zapisuje ${lines[0]}")
            }
            else if(x == 1){
                myFile.appendText("${(lines[1].toInt()+1)}\n")
                Log.d("TournamentFragment","na $x zapisuje ${lines[1].toInt()+1}")
            }
            else if(lines[x].split(":")[0] == playerA)
            {
                myFile.appendText("${lines[x].split(":")[0]}:${lines[x].split(":")[1].toInt()  + aPoint}\n")
                Log.d("TournamentFragment","na $x zapisuje ${lines[x].split(":")[0]}:${lines[x].split(":")[1].toInt() + aPoint}")
            }
            else if(lines[x].split(":")[0] == playerB)
            {
                myFile.appendText("${lines[x].split(":")[0]}:${lines[x].split(":")[1].toInt() + bPoint}\n")
                Log.d("TournamentFragment","na $x zapisuje ${lines[x].split(":")[0]}:${lines[x].split(":")[1].toInt()  + aPoint}")
            }
            else if(1<x && x<2+lines[1].toInt()) {
                if(lines[x].split(":")[0] == playerA)
                {
                    myFile.appendText("${lines[x].split(":")[0]}:${lines[x].split(":")[1].toInt() + aPoint}\n")
                    Log.d("TournamentFragment","na $x zapisuje ${lines[x].split(":")[0]}:${lines[x].split(":")[1].toInt() + aPoint}")
                }
                else if(lines[x].split(":")[0] == playerB)
                {
                    myFile.appendText("${lines[x].split(":")[0]}:${lines[x].split(":")[1].toInt() + bPoint}\n")
                    Log.d("TournamentFragment","na $x zapisuje ${lines[x].split(":")[0]}:${lines[x].split(":")[1].toInt() + bPoint}")
                }
                else
                {
                    myFile.appendText("${lines[x]}\n")
                }
            }
            else if(x == 2+lines[1].toInt()+lines[0].toInt())
            {
                myFile.appendText("${lines[x]}:$aPoint;$bPoint\n")
                Log.d("TournamentFragment","na $x zapisuje ${lines[x]}:$aPoint;$bPoint")

            }
            else {
                myFile.appendText("${lines[x]}\n")
                Log.d("TournamentFragment", "na $x zapisano ${lines[x]}")
            }
        }

        Log.d("TournamentFragment", "CO JEST W PLIKU!!!!!")
        val lines2 = myFile.bufferedReader().readLines()
        for(x in lines2)
        {
            Log.d("TournamentFragment", x)
        }
    }


}