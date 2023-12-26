package com.eduardomcb.tictactoe.model

import com.eduardomcb.tictactoe.Player

data class Winner(
    var currentPlayer: Player = Player.EMPTY,
    var isWon: Boolean = false
)
