package com.example.reversi

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.io.File


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


}