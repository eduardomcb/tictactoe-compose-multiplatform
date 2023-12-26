package com.eduardomcb.tictactoe.model

import com.eduardomcb.tictactoe.Player

data class Cell(
    val row: Int,
    val column: Int,
    val player: Player
)

