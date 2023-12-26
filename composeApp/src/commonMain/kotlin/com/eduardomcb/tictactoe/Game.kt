package com.eduardomcb.tictactoe

import com.eduardomcb.tictactoe.model.Cell
import com.eduardomcb.tictactoe.model.Winner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

class Game : ViewModel() {
    private val _winner = MutableStateFlow(Winner())
    val winner = _winner.asStateFlow()

    private val _currentPlayer: MutableStateFlow<Player> = MutableStateFlow(Player.X)
    val currentPlayer = _currentPlayer.asStateFlow()

    private val _cells: MutableStateFlow<Array<Array<Cell>>> =
        MutableStateFlow(Array(3) { Array(3) { Cell(row = 0, column = 0, player = Player.EMPTY) } })
    val cells = _cells.asStateFlow()

    fun makeMove(row: Int, col: Int): Boolean {
        println("makeMoke")
        if (_cells.value[row][col].player == Player.EMPTY) {
            _cells.value[row][col] = Cell(row = row, column = col, player = _currentPlayer.value)
            _currentPlayer.value = if (_currentPlayer.value == Player.X) Player.O else Player.X
            checkForWin()
            return true
        }
        return false
    }

    fun makeAIMove() {
        println("makeAIMove")
        val emptyCells = mutableListOf<Cell>()

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (_cells.value[i][j].player == Player.EMPTY) {
                    emptyCells.add(Cell(row = i, column = j, player = Player.EMPTY))
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val randomCell = emptyCells.random()
            if (_cells.value[randomCell.row][randomCell.column].player == Player.EMPTY) {
                _cells.value[randomCell.row][randomCell.column] =
                    Cell(row = randomCell.row, column = randomCell.column, player = _currentPlayer.value)
                _currentPlayer.value = if (_currentPlayer.value == Player.X) Player.O else Player.X
                checkForWin()
            }
        }
    }

    fun checkForWin() {
        // Check rows
        for (i in 0 until 3) {
            if (_cells.value[i][0].player != Player.EMPTY &&
                _cells.value[i][0].player == _cells.value[i][1].player &&
                _cells.value[i][1].player == _cells.value[i][2].player
            ) {
                _winner.value = Winner(currentPlayer = _cells.value[i][0].player, isWon = true)
                return
            }
        }
        // Check columns
        for (i in 0 until 3) {
            if (_cells.value[0][i].player != Player.EMPTY &&
                _cells.value[0][i].player == _cells.value[1][i].player &&
                _cells.value[1][i].player == _cells.value[2][i].player
            ) {
                _winner.value = Winner(currentPlayer = _cells.value[0][i].player, isWon = true)
                return
            }
        }
        // Check diagonals
        if (_cells.value[0][0].player != Player.EMPTY &&
            _cells.value[0][0].player == _cells.value[1][1].player &&
            _cells.value[1][1].player == _cells.value[2][2].player
        ) {
            _winner.value = Winner(currentPlayer = _cells.value[0][0].player, isWon = true)
            return
        }
        if (_cells.value[0][2].player != Player.EMPTY &&
            _cells.value[0][2].player == _cells.value[1][1].player &&
            _cells.value[1][1].player == _cells.value[2][0].player
        ) {
            _winner.value = Winner(currentPlayer = _cells.value[0][2].player, isWon = true)
            return
        }
        // Check for a tie
        if (_cells.value.all { row ->
                row.all {
                    it.player != Player.EMPTY
                }
            }) {
            _winner.value = Winner(currentPlayer = Player.TIE, isWon = true)
        }
    }


    fun reset() {
        _winner.value = Winner(currentPlayer = Player.EMPTY, isWon = false)
        _currentPlayer.value = Player.X
        _cells.value = Array(3) { row ->
            Array(3) { col ->
                Cell(row = row, column = col, player = Player.EMPTY)
            }
        }
    }
}