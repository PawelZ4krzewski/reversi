package com.example.reversi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import kotlin.system.exitProcess

class Menu : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        view.findViewById<Button>(R.id.start1PlayerGameButton).setOnClickListener(){
            view.findNavController().navigate(R.id.action_menu_to_game1PlayerFragment)
        }

        view.findViewById<Button>(R.id.start2PlayerGameButton).setOnClickListener(){
            view.findNavController().navigate(R.id.action_menu_to_gameFragment)
        }

        view.findViewById<Button>(R.id.settingsButton).setOnClickListener(){
            view.findNavController().navigate(R.id.action_menu_to_settingsFragment)
        }

        view.findViewById<Button>(R.id.buttonTournament).setOnClickListener(){
            view.findNavController().navigate(R.id.action_menu_to_tournamentFragment)
        }

        view.findViewById<Button>(R.id.buttonRanking).setOnClickListener(){
            view.findNavController().navigate(R.id.action_menu_to_rankingFragment)
        }

        view.findViewById<Button>(R.id.exitButton).setOnClickListener()
        {
            exitProcess(-1)
        }
        return view
    }



}