package com.example.reversi

class Player(color: Char, val name:String)
{
    val colorOfPawn = color
    var amountOfPawns: Int = 2

    public fun putPawn()
    {
        amountOfPawns++
    }
    public fun wastePawn()
    {
        amountOfPawns--
    }
}