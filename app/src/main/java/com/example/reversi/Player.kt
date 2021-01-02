package com.example.reversi

class Player(color: Char)
{
    val colorOfPawn = color
    var amountOfPawns: Int = 2

    public fun putPawn()
    {
        amountOfPawns++
    }
}