package com.example.reversi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_settings.*
import java.io.File
import kotlin.concurrent.thread


class SettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        readOption(view)

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
        thread{
            val myFile = File(requireContext().filesDir, "gameSettings.txt")
            val isFile = myFile.createNewFile()
            if (isFile) {
                Log.d("SettingsFragment", "Utworzono nowy plik")
            } else {
                Log.d("SettingsFragment", "Plik juz istnieje")
            }

            myFile.writeText("boardSize ")
            myFile.appendText("$boardSize\n")
            myFile.appendText("playerAColor $playerAColor\n")
            myFile.appendText("playerBColor $playerBColor\n")
        }
    }

    private fun readOption(v: View)
    {

        val myFile = File(requireContext().filesDir,"gameSettings.txt")
        val isFile = myFile.createNewFile()
        if(!isFile)
        {
            Log.d("SettingsFragment", "Odczytuje z pliku")

            val tekst = myFile.bufferedReader().readLines()
            for(x in tekst)
            {
                val settingLine = x.split(" ")

                when(settingLine[0])
                {
                    "boardSize" -> {
                        when(settingLine[1])
                        {
                            "4" ->
                            {
                                v.findViewById<RadioButton>(R.id.radioButtonBoardSize4).isChecked=true
                            }
                            "6" ->
                            {
                                v.findViewById<RadioButton>(R.id.radioButtonBoardSize6).isChecked=true
                            }
                            "8" ->
                            {
                                v.findViewById<RadioButton>(R.id.radioButtonBoardSize8).isChecked=true
                            }
                        }
                    }
                    "playerAColor" -> {
                        when(settingLine[1]) {
                            "B" -> {
                                v.findViewById<RadioButton>(R.id.radioButtonBlack).isChecked = true
                            }
                            "R" -> {
                                v.findViewById<RadioButton>(R.id.radioButtonRed).isChecked = true
                            }
                            "DW" -> {
                                v.findViewById<RadioButton>(R.id.radioButtonDarKWood).isChecked =
                                    true
                            }
                        }
                    }
                    "playerBColor" -> {
                        when(settingLine[1]) {
                            "W" -> {
                                v.findViewById<RadioButton>(R.id.radioButtonWhite).isChecked = true
                            }
                            "Y" -> {
                                v.findViewById<RadioButton>(R.id.radioButtonYellow).isChecked = true
                            }
                            "LW" -> {
                                v.findViewById<RadioButton>(R.id.radioButtonLightWood).isChecked =
                                    true
                            }
                        }
                    }
                }
            }

        }


    }
}