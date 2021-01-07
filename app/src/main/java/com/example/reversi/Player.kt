package com.example.reversi

class Player(color: Char, name: String) {
    val color = color
    val name = name
    var amountOfPawns: Int = 2

    public fun putPawn() {
        amountOfPawns++
    }

    public fun wastePawn() {
        amountOfPawns--
    }
}