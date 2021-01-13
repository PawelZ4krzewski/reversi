package com.example.reversi

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext

class PawnButton(val button: ImageButton, var color: String, val x: Int, val y: Int) {


    fun setColor(color: String): Int {
        when (color) {
            "W" -> {
                this.color = color
                return R.drawable.ic_white_pawn
            }
//            "Y" -> {
//                this.color = color
//                return "#FFFF00"
//            }
//            "LW" -> {
//                this.color = color
//                return "#EEFF00"
//            }
            "B" -> {
                this.color = color
                return R.drawable.ic_black_pawn
            }
//            "R" -> {
//                this.color = color
//                return "#FF0000"
//            }
//            "DW" -> {
//                this.color = color
//                return "#FF0430"
//            }
        }
        return R.drawable.ic_black_pawn
    }
}