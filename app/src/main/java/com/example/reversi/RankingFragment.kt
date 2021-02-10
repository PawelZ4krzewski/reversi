package com.example.reversi

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
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.navigation.findNavController
import java.io.File
import java.nio.file.Files.size
import kotlin.math.roundToInt


class RankingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ranking, container, false)
        val backButton = view.findViewById<Button>(R.id.buttonBacktoMenuFromRanking)

        backButton.setOnClickListener()
        {
            view.findNavController().navigate(R.id.action_rankingFragment_to_menu)
        }

        createTable(view)

        return view
    }

    fun createTable(v: View)
    {
        val rankingLinearLayout = v.findViewById<LinearLayout>(R.id.linearLayoutRankingBoard)

        val myFile = File(requireContext().filesDir,"ranking.txt")
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

        for(i in 0..tekst.size)
        {
            for(j in i..tekst.size-1)
            {
                val a = tekst[i].split(";")
                val b = tekst[j].split(";")
                if(a[1].toInt()<b[1].toInt())
                {
                    val temp = tekst[i]
                    tekst[i]=tekst[j]
                    tekst[j]=temp
                }
            }
        }

        var i = 1;
        for (x in tekst) {

            val row = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                orientation = LinearLayout.HORIZONTAL
            }

            val rankingLine = x.split(";")

            val number = TextView(requireContext()).apply{
                layoutParams = LinearLayout.LayoutParams((50f).dpToPixels().roundToInt(),android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "$i."
                setTextColor(Color.parseColor("#FFFFFF"))
                gravity =  Gravity.CENTER_HORIZONTAL
                textSize = 18f
            }

            val playerName = TextView(requireContext()).apply{
                layoutParams = LinearLayout.LayoutParams((175f).dpToPixels().roundToInt(),android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "${rankingLine[0]}"
                setTextColor(Color.parseColor("#FFFFFF"))
                textSize = 18f
            }

            playerName.setGravity(Gravity.CENTER)

            val score = TextView(requireContext()).apply{
                layoutParams = LinearLayout.LayoutParams((100f).dpToPixels().roundToInt(),android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
                text = "${rankingLine[1]}"
                setTextColor(Color.parseColor("#FFFFFF"))
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
            rankingLinearLayout.addView(row)
            i++
        }

    }

    private fun Float.dpToPixels(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }
}