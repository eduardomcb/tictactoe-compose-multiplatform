package com.eduardomcb.tictactoe.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PanoramaFishEye
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eduardomcb.tictactoe.Player
import com.eduardomcb.tictactoe.model.Cell

@Composable
fun Cell(cell: Cell, modifier: Modifier = Modifier, onClick: (cell: Cell) -> Unit) {
    Card(
        modifier = modifier
            .clickable {
                onClick(cell)
            }
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (cell.player) {
                Player.X -> {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                    )
                }

                Player.O -> {
                    Icon(
                        imageVector = Icons.Outlined.PanoramaFishEye,
                        contentDescription = null,
                        tint = Color.Blue,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                    )
                }

                else -> {
//                println("Nothing")
                }
            }

        }
    }
}