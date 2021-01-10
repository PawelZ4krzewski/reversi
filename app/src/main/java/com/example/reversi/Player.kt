package com.example.reversi

open class Player(color: String, name: String) {
    val color = color
    private var name = name
    var amountOfPawns: Int = 2

    fun setName(playerName: String)
    {
        this.name = playerName
    }

    fun getName() :String
    {
        return this.name
    }


}