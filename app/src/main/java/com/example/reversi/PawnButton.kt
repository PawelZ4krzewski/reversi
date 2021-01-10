package com.example.reversi

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext

class PawnButton(val button: Button, var color: String, val x: Int, val y: Int) {


    fun setColor(color: String): String? {
        when (color) {
            "W" -> {
                this.color = color
                return "#ffffff"
            }
            "Y" -> {
                this.color = color
                return "#FFFF00"
            }
            "LW" -> {
                this.color = color
                return "#EEFF00"
            }
            "B" -> {
                this.color = color
                return "#000000"
            }
            "R" -> {
                this.color = color
                return "#FF0000"
            }
            "DW" -> {
                this.color = color
                return "#FF0430"
            }
        }
        return "#006200"
    }
}