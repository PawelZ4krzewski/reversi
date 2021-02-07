package com.example.reversi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import java.io.File

class LoginRegister : Fragment() {

    private val itemViewModel: ItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_register, container, false)

        val registerButton = view.findViewById<Button>(R.id.buttonRegistry)
        val loginButton = view.findViewById<Button>(R.id.buttonLogin)

        val errorText = view.findViewById<TextView>(R.id.textViewError)
        val login = view.findViewById<EditText>(R.id.editTextLogin)
        val password = view.findViewById<EditText>(R.id.editTextPassword)


        val myFile = File(requireContext().filesDir,"users.txt")
        val isFile = myFile.createNewFile()
        if(isFile)
        {
            Log.d("LoginRegister", "Utworzono nowy plik")
        }
        else
        {
            Log.d("LoginRegister", "Plik juz istnieje")
        }

        loginButton.setOnClickListener()
        {
            val tekst = myFile.bufferedReader().readLines()
            var isNotExistUser = false;

            if((login.text.toString().trim() == "") || (password.text.toString().trim() == ""))
            {
                errorText.text = "Login and Password cannot be empty"
            }

            for(x in tekst)
            {
                val pom = x.split(" ")
                if(pom[0] == login.text.toString())
                {
                    isNotExistUser = true
                    if(pom[1] == password.text.toString())
                    {
                        itemViewModel.player = login.text.toString()
                        view.findNavController().navigate(R.id.action_loginRegister_to_menu)
                    }
                    else
                    {
                        errorText.text = "Incorrect password"
                    }
                }
            }

            if(!isNotExistUser)
            {
                errorText.text = "User is not exist"
            }
        }


        registerButton.setOnClickListener()
        {
            val tekst = myFile.bufferedReader().readLines()
            var isExitUser = false;
            for(x in tekst)
            {
                val pom = x.split(" ")
                if(pom[0] == login.text.toString())
                {
                    isExitUser = true
                    errorText.text = "Can't register user. This user is already in database"
                    break
                }
            }

            if(!isExitUser)
            {
                if((login.text.toString().trim() != "") && (password.text.toString().trim() != ""))
                {
                    myFile.appendText("${login.text} ${password.text}\n")
                    errorText.text = "Correct create user"
                }
                else
                {
                    errorText.text = "Login and Password cannot be empty"
                }

            }
        }


        return view
    }

}