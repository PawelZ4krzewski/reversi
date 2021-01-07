package com.example.reversi

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext

class PawnButton(val button: Button, var color: Char, val x: Int, val y: Int) {


    fun setColor(color: Char): String? {
        when (color) {
            'W' -> {
                this.color = color
                return "#ffffff"
            }
            'B' -> {
                this.color = color
                return "#000000"
            }
        }
        return "#006200"
    }
}