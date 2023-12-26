package com.eduardomcb.tictactoe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eduardomcb.tictactoe.components.Cell
import com.eduardomcb.tictactoe.model.Cell
import com.eduardomcb.tictactoe.theme.AppTheme
import com.eduardomcb.tictactoe.theme.LocalThemeIsDark
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.rememberImageSuccessPainter
import com.seiko.imageloader.ui.AutoSizeBox
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun App() = AppTheme {
    val viewModel = remember { Game() }
    var isDark by LocalThemeIsDark.current

    val currentPlayer by viewModel.currentPlayer.collectAsState()
    val cells by viewModel.cells.collectAsState()
    val winner by viewModel.winner.collectAsState()

    val (isMoved, setMoved) = remember { mutableStateOf(false) }
    val (isMachine, setMachine) = remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.checkForWin()
    }

    LaunchedEffect(isMoved) {
        if (isMoved && isMachine) {
            viewModel.makeAIMove()
            setMoved(false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            val richTooltipState = remember { RichTooltipState() }
            val scope = rememberCoroutineScope()

            RichTooltipBox(
                text = {
                    Text(text = "See my portfolio on github")
                },
                title = { Text(text = "Hi! :-)") },
                action = {
                    TextButton(onClick = {
                        scope.launch {
                            richTooltipState.dismiss()
                            openUrl("https://github.com/eduardomcb")
                        }
                    }
                    ) {
                        Text(text = "Visit")
                    }
                },
                tooltipState = richTooltipState
            ) {
                AutoSizeBox("https://avatars.githubusercontent.com/u/116934175?v=4") { action ->
                    when (action) {
                        is ImageAction.Success -> {
                            Image(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(35.dp)
                                    .tooltipAnchor()
                                    .clip(shape = CircleShape)
                                    .clickable {
                                        scope.launch {
                                            richTooltipState.show()
                                        }
                                    },
                                painter = rememberImageSuccessPainter(action),
                                contentDescription = null
                            )
                        }

                        is ImageAction.Loading -> {
                            CircularProgressIndicator()
                        }

                        is ImageAction.Failure -> {

                        }
                    }
                }
            }

            Text(
                text = "Player ${currentPlayer.name}'s turn",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                textAlign = TextAlign.Center
            )

            PlainTooltipBox(
                tooltip = { Text("Reset") }
            ) {
                IconButton(
                    onClick = {
                        viewModel.reset()
                        setMachine(false)
                        setMoved(false)
                    },
                    modifier = Modifier.tooltipAnchor()
                ) {
                    Icon(
                        modifier = Modifier.padding(8.dp).size(20.dp),
                        imageVector = Icons.Outlined.RestartAlt,
                        contentDescription = null
                    )
                }
            }

            PlainTooltipBox(
                tooltip = { Text("Change theme") }
            ) {
                IconButton(
                    onClick = { isDark = !isDark }
                ) {
                    Icon(
                        modifier = Modifier.padding(8.dp).size(20.dp),
                        imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = null
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row {

                OutlinedButton(
                    onClick = {
                        setMachine(false)
                    },
                    shape = RoundedCornerShape(
                        topStart = 32.dp,
                        bottomStart = 32.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMachine) Color.Transparent else MaterialTheme.colorScheme.primary
                    ),
                    border = if (isMachine) BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    ) else BorderStroke(
                        width = 1.dp,
                        Color.Transparent
                    )
                ) {
                    Text(
                        "Human",
                        color = if (isMachine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    )
                }

                OutlinedButton(
                    onClick = {
                        setMachine(true)
                    },
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 32.dp,
                        bottomEnd = 32.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMachine) MaterialTheme.colorScheme.primary else Color.Transparent
                    ),
                    border = if (isMachine) BorderStroke(
                        width = 1.dp,
                        Color.Transparent
                    ) else BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                ) {
                    Text(
                        "Machine",
                        color = if (isMachine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            for (i in 0 until 3) {
                Row {
                    for (j in 0 until 3) {
                        val cell = Cell(row = i, column = j, player = cells[i][j].player)
                        Cell(
                            cell = cell,
                            onClick = {
                                setMoved(viewModel.makeMove(it.row, it.column))
                            },
                            modifier = Modifier
                                .size(50.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }

            if (winner.isWon) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (winner.currentPlayer == Player.TIE) {
                        Text(
                            text = "It's a tie!",
                            style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier
                                .padding(16.dp),
                        )
                    } else {
                        Text(
                            text = "Player ${winner.currentPlayer} wins!",
                            style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier
                                .padding(16.dp),
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.reset()
                            setMachine(false)
                            setMoved(false)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Play Again")
                    }
                }
            }
        }
    }
}

internal expect fun openUrl(url: String?)