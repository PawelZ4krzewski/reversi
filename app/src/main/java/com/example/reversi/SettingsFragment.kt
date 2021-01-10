package com.example.reversi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_settings.*
import java.io.File


class SettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        view.findViewById<Button>(R.id.buttonCheck).setOnClickListener()
        {
            val boardSizeId = radioGroupBoardSize.checkedRadioButtonId
            var boardSizeInt: Int = 8
            var playerAColorStr = "B"
            var playerBColorStr = "W"

//            Log.d("SettingsFragment", "Rozmiar tablice to: ${boardSizeId}")
            if (boardSizeId != -1) {
//                val boardSize = view.findViewById<RadioButton>(boardSizeId)
                val boardSize = resources.getResourceEntryName(boardSizeId)

                when (boardSize) {
                    "radioButtonBoardSize8" -> {
                        boardSizeInt = 8
                    }
                    "radioButtonBoardSize6" -> {
                        boardSizeInt = 6
                    }
                    "radioButtonBoardSize4" -> {
                        boardSizeInt = 4
                    }
                }
            }
            Log.d("SettingsFragment", "Rozmiar tablicy to: ${boardSizeInt}")



            val playerAColorId = radioGroupColorPlayerA.checkedRadioButtonId
            if (playerAColorId != -1) {
                val playerAColor = resources.getResourceEntryName(playerAColorId)

                when (playerAColor) {
                    "radioButtonBlack" -> {
                        playerAColorStr = "B"
                    }
                    "radioButtonRed" -> {
                        playerAColorStr = "R"
                    }
                    "radioButtonDarKWood" -> {
                        playerAColorStr = "DW"
                    }
                }
            }
            Log.d("SettingsFragment", "Rozmiar tablicy to: ${playerAColorStr}")

            val playerBColorId = radioGroupColorPlayerB.checkedRadioButtonId
            if (playerBColorId != -1) {
                val playerBColor = resources.getResourceEntryName(playerBColorId)

                when (playerBColor) {
                    "radioButtonWhite" -> {
                        playerBColorStr = "W"
                    }
                    "radioButtonYellow" -> {
                        playerBColorStr = "Y"
                    }
                    "radioButtonLightWood" -> {
                        playerBColorStr = "LW"
                    }
                }
            }
            Log.d("SettingsFragment", "Rozmiar tablicy to: ${playerBColorStr}")

            saveOption(boardSizeInt,playerAColorStr,playerBColorStr)
            view.findNavController().navigate(R.id.action_settingsFragment_to_menu)
        }

        return view
    }

    private fun saveOption(boardSize: Int, playerAColor: String, playerBColor: String)
    {

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
//        for(x in tekst)
//        {
//            Log.d("SettingsFragment", "LINIA: $x")
//        }
        myFile.writeText("boardSize ")
        myFile.appendText("$boardSize\n")
        myFile.appendText("playerAColor $playerAColor\n")
        myFile.appendText("playerBColor $playerBColor\n")
    }

}