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
            "Y" -> {
                this.color = color
                return R.drawable.ic_yellow_pawn
            }
            "LW" -> {
                this.color = color
                return R.drawable.ic_lw_pawn
            }
            "B" -> {
                this.color = color
                return R.drawable.ic_black_pawn
            }
            "R" -> {
                this.color = color
                return R.drawable.ic_red_pawn
            }
            "DW" -> {
                this.color = color
                return R.drawable.ic_dw_pawn
            }
        }
        return R.drawable.ic_black_pawn
    }
}