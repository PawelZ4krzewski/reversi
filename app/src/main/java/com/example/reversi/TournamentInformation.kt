package com.example.reversi

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.io.File
import kotlin.math.roundToInt


class TournamentInformation : Fragment() {

    val args: TournamentInformationArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tournament_information, container, false)
        val tournamentName = args.tournamentName.toString()
        val amountOfGames = view.findViewById<TextView>(R.id.textViewAmountOfGames)
        val bestPlayerName = view.findViewById<TextView>(R.id.textViewBestPlayerName)
        val currentGame = view.findViewById<TextView>(R.id.textViewCurrentGameName)
        val startTournamentGame = view.findViewById<Button>(R.id.buttonStartTournamentGame)
        val openRankingTournament = view.findViewById<Button>(R.id.buttonRanking)
        val openLastGames = view.findViewById<Button>(R.id.buttonLastGames)
        val exitToMenuButton = view.findViewById<Button>(R.id.buttonBackToMenuTI)


        val lines = readTournamentInformation(tournamentName)

        view.findViewById<TextView>(R.id.textViewTournamentName).text = tournamentName
        amountOfGames.text = "${lines[1]} / ${getAmountOfGames(lines[0].toInt())}"
        bestPlayerName.text = findTheBestPlayer(lines)
        currentGame.text = if (2+lines[1].toInt()+lines[0].toInt() < lines.size) lines[2+lines[1].toInt()+lines[0].toInt()] else "Every games were played"

        if(lines[1].toInt() < getAmountOfGames(lines[0].toInt()))
        {
            startTournamentGame.setOnClickListener()
            {
                Log.d("TournamentFragment", "Zaczynamy zabawe")
                view.findNavController().navigate(TournamentInformationDirections.actionTournamentInformationToTournamentGame22(tournamentName))
            }
        }
        else
        {
            startTournamentGame.setBackgroundColor(Color.parseColor("#C0C0C0"))
        }


        openLastGames.setOnClickListener()
        {
            openLastGamesDialog(view, tournamentName)
        }
        openRankingTournament.setOnClickListener()
        {
            openRankingDialog(view, tournamentName)
        }


        exitToMenuButton.setOnClickListener()
        {
            view.findNavController().navigate(R.id.action_tournamentInformation_to_menu)
        }

        return view
    }


    private fun getAmountOfGames(n: Int) : Int
    {
        return n*(n-1)/2
    }

    private fun findTheBestPlayer(lines: List<String>): String
    {
        var name = ""
        var score = 0
        for(i in 2 until 1+lines[0].toInt())
        {

            val xName = lines[i].split(":")[0]
            val xScore = lines[i].split(":")[1].toInt()
            if(xScore> score)
            {
                score = xScore
                name = xName
            }
        }

        if(score > 0 )
        {
            return name
        }
        else
        {
            return "Nobody is the best"
        }
    }

    private fun readTournamentInformation(tournamentName: String) : List<String>
    {
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
        return myFile.bufferedReader().readLines()
    }

    private fun openLastGamesDialog(view: View, fileName: String) {

        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(requireContext())
        val v = layoutInflater.inflate(R.layout.last_games_tournament, null)
        val linearLayoutLastGames = v.findViewById<LinearLayout>(R.id.linearLayoutLastGames)
        val myFile = File(requireContext().filesDir,"$fileName.txt")
        val isFile = myFile.createNewFile()
        if(isFile)
        {
            Log.d("RankingFragment", "Utworzono nowy plik")
        }
        else
        {
            Log.d("RankingFragment", "Plik juz istnieje")
        }

        val tekst = myFile.bufferedReader().readLines().toMutableList()
        val startRead = tekst[0].toInt()+2
        var i = 0;

        for (x in tekst) {
            Log.d("TourInformation", "Wchodze do petli tekst")

            val row = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                orientation = LinearLayout.HORIZONTAL
            }
            Log.d("TourInformation", "Utworzylem row")
            if(i<startRead)
            {
                Log.d("TourInformation", "Za mala Linia $i")
                i++
                continue

            }
            val lastGamesLine = x.split(":")

            Log.d("TourInformation", "podzielilem ${lastGamesLine[0]}")

            if(lastGamesLine.size<2)
            {
                Log.d("TourInformation", "Nie odbyl sie $lastGamesLine")
                continue
            }

            val players = lastGamesLine[0].split(" vs ")
            val score = lastGamesLine[1].split(";")
            Log.d("TourInformation", "playerA ${players[0]} ")
            Log.d("TourInformation", "playerB ${players[1]} ")
            Log.d("TourInformation", "scoreA ${score[0]}")
            Log.d("TourInformation", "scoreB ${players[0]}")
            val playerA = TextView(requireContext()).apply{
                layoutParams = LinearLayout.LayoutParams((80f).dpToPixels().roundToInt(),android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "${players[0]}"
                gravity =  Gravity.CENTER_HORIZONTAL
                textSize = 18f
            }
            Log.d("TourInformation", "Tworze PlayerA")

            val scoreA = TextView(requireContext()).apply{
                layoutParams = LinearLayout.LayoutParams((80f).dpToPixels().roundToInt(),android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "${score[0]}"
                textSize = 18f
            }

            scoreA.setGravity(Gravity.CENTER)
//            scoreA.setBackgroundColor(Color.parseColor("#443512"))

            val scoreB = TextView(requireContext()).apply{
                layoutParams = LinearLayout.LayoutParams((80f).dpToPixels().roundToInt(),android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "${score[1]}"
                textSize = 18f
            }
            scoreB.gravity = Gravity.CENTER_HORIZONTAL

            val playerB = TextView(requireContext()).apply{
                layoutParams = LinearLayout.LayoutParams((80f).dpToPixels().roundToInt(),android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "${players[1]}"
                textSize = 18f
            }
            playerB.gravity = Gravity.CENTER_HORIZONTAL

            row.addView(playerA)
            row.addView(scoreA)
            row.addView(scoreB)
            row.addView(playerB)
            linearLayoutLastGames.addView(row)
            i++
        }


        val buttonExit = v.findViewById<ImageButton>(R.id.imageButtonCloseLastGames)
        buttonExit.setOnClickListener()
        {
            Log.d("TournamentInformation", "EXIT LAST GAMES")
            dialog?.dismiss()
        }

        builder.setView(v)
        dialog = builder.create()
        dialog.show()
    }

    private fun openRankingDialog(view: View, fileName: String) {

        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(requireContext())
        val v = layoutInflater.inflate(R.layout.ranking_tournament, null)
        val linearLayoutRanking = v.findViewById<LinearLayout>(R.id.linearLayoutRankingTournament)
        val myFile = File(requireContext().filesDir,"$fileName.txt")
        val isFile = myFile.createNewFile()
        if(isFile)
        {
            Log.d("RankingFragment", "Utworzono nowy plik")
        }
        else
        {
            Log.d("RankingFragment", "Plik juz istnieje")
        }

        val tekst = myFile.bufferedReader().readLines().toMutableList()

        for(i in 2..tekst[0].toInt()+1)
        {
            for(j in i..tekst[0].toInt())
            {
                val a = tekst[i].split(":")
                val b = tekst[j].split(":")
                if(a[1].toInt()<b[1].toInt())
                {
                    val temp = tekst[i]
                    tekst[i]=tekst[j]
                    tekst[j]=temp
                }
            }
        }

        var i = 1;
        var j = 1;
        for (x in tekst) {

            if(i <= 2)
            {
                i++
                continue
            }

            if(i>tekst[0].toInt()+2)
            {
                break
            }

            val row = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                orientation = LinearLayout.HORIZONTAL
            }

            val rankingLine = x.split(":")

            val number = TextView(requireContext()).apply{
                layoutParams = LinearLayout.LayoutParams((45f).dpToPixels().roundToInt(),android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "$j."
                gravity =  Gravity.CENTER_HORIZONTAL
                textSize = 18f
            }

            val playerName = TextView(requireContext()).apply{
                layoutParams = LinearLayout.LayoutParams((175f).dpToPixels().roundToInt(),android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "${rankingLine[0]}"
                textSize = 18f
            }

            playerName.setGravity(Gravity.CENTER)

            val score = TextView(requireContext()).apply{
                layoutParams = LinearLayout.LayoutParams((100f).dpToPixels().roundToInt(),android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "${rankingLine[1]}"
                textSize = 18f
            }
            score.gravity = Gravity.CENTER_HORIZONTAL

            if(i<=3){
                number.setTypeface(null, Typeface.BOLD);
                playerName.setTypeface(null, Typeface.BOLD);
                score.setTypeface(null, Typeface.BOLD);
            }
            row.addView(number)
            row.addView(playerName)
            row.addView(score)
            linearLayoutRanking.addView(row)
            i++
            j++
        }


        val buttonExit = v.findViewById<ImageButton>(R.id.imageButtonCloseRankingTournament)
        buttonExit.setOnClickListener()
        {
            Log.d("TournamentInformation", "EXIT LAST GAMES")
            dialog?.dismiss()
        }

        builder.setView(v)
        dialog = builder.create()
        dialog.show()
    }
    
    private fun Float.dpToPixels(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }
}