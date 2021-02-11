package com.example.reversi

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import org.w3c.dom.Text
import java.io.File
import kotlin.math.log

class TournamentFragment : Fragment() {

    private val itemViewModel: ItemViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tournament, container, false)
        val myLayout = view.findViewById<LinearLayout>(R.id.linearLayoutPlayersInTournament)
        val namePlayerEditText = view.findViewById<EditText>(R.id.editTextNextPlayerName)
        val viewError = view.findViewById<TextView>(R.id.textViewErrorTournament)
        val addPlayerButton = view.findViewById<Button>(R.id.buttonAddPlayerToTournament)
        val startTournamentFragment = view.findViewById<Button>(R.id.buttonStartTournament)
        val loadTournament = view.findViewById<Button>(R.id.buttonLoadTournament)
        val backToMenu = view.findViewById<Button>(R.id.buttonExitToMenu)
        val everyPlayer = mutableListOf(itemViewModel.player)

        view.findViewById<TextView>(R.id.textViewAdmin).apply {
            textSize = 20f
            text = itemViewModel.player
        }

        addPlayerButton.setOnClickListener(){

            if(namePlayerEditText.text.trim() != "")
            {
                var isExist = false
                for(x in everyPlayer)
                {
                    if(namePlayerEditText.text.trim().toString() == x)
                    {
                        isExist= true
                        break
                    }
                }

                if(!isExist) {

                    val textView = TextView(requireContext()).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        id = View.generateViewId()
                        textSize = 20f
                        setTextColor(Color.parseColor("#FFFFFF"))
                    }
                   textView.setText(namePlayerEditText.text.trim())

                    myLayout.addView(textView)
                    everyPlayer.add(textView.text.toString())
                    Log.d("TournamentFragment", "dodaje tekst o tresci ${textView.text}")
                    namePlayerEditText.text = null
                }
                else
                {
                    Log.d("TournamentFragment", "Taki gracz juz istnieje")
                }
            }
            else
            {
                Log.d("TournamentFragment", "Imie nie moze byc puste")
            }
        }

        startTournamentFragment.setOnClickListener()
        {
            Log.d("TournamentFragment", "START")
            val fileName = view.findViewById<EditText>(R.id.editTextTournament).text.toString()
            val myFile = File(requireContext().filesDir,"$fileName.txt")
            val isFile = myFile.createNewFile()
            if(isFile) {
                Log.d("TournamentFragment", "Utworzono nowy plik o nazwie $fileName.txt")

                myFile.writeText("${everyPlayer.size}\n") //amount of Players
                myFile.appendText("0\n") //amount of games which are completed

                for (x in everyPlayer) {
                    myFile.appendText("$x:0\n")
                    Log.d("TournamentFragment", x)
                }

                for (i in 0 until everyPlayer.size - 1) {
                    for (j in i + 1 until everyPlayer.size) {
                        myFile.appendText("${everyPlayer[i]} vs ${everyPlayer[j]}\n")
                        Log.d("TournamentFragment", "${everyPlayer[i]} vs ${everyPlayer[j]}\n")
                    }
                }
                Log.d("TournamentFragment", "NAZWA PLIKU ${fileName}\n")
                view.findNavController().navigate(
                    TournamentFragmentDirections.actionTournamentFragmentToTournamentInformation(
                        fileName
                    )
                )
            }
            else
            {
                Log.d("TournamentFragment", "Taki turniej juz istnieje, podaj inną nazwe")
                viewError.text = "$fileName already exist. Give other name."
                viewError.setBackgroundColor(Color.parseColor("#000000"))
            }
        }

        loadTournament.setOnClickListener()
        {
            val fileName = view.findViewById<EditText>(R.id.editTextTournament).text.toString()
            val myFile = File(requireContext().filesDir, "$fileName.txt")
            val isFile = myFile.exists()
            if(isFile)
            {
                Log.d("TournamentFragment", "Wczytuje plik o nazwie $fileName.txt.")
                view.findNavController().navigate(
                    TournamentFragmentDirections.actionTournamentFragmentToTournamentInformation(
                        fileName
                    )
                )
            }
            else
            {
                Log.d("TournamentFragment", "$fileName deosn't exist. Give other name")
                viewError.text = "$fileName deosn't exist. Give other name"
                viewError.setBackgroundColor(Color.parseColor("#000000"))
            }
        }


        backToMenu.setOnClickListener()
        {
            view.findNavController().navigate(R.id.action_tournamentFragment_to_menu)
        }

        return view
    }



}