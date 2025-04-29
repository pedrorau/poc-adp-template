package com.pedrorau.featurefallingletters.presentation.main

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pedrorau.featurefallingletters.presentation.constants.Constants.BUTTON_TEXT
import com.pedrorau.featurefallingletters.presentation.constants.Constants.INPUT_PLACEHOLDER
import com.pedrorau.featurefallingletters.presentation.constants.Constants.MAX_CHARS_TO_TAKE
import com.pedrorau.featurefallingletters.presentation.constants.Constants.MAX_COLUMNS_NUMBER
import com.pedrorau.featurefallingletters.presentation.constants.Constants.MAX_ROWS_NUMBER
import com.pedrorau.featurefallingletters.presentation.constants.Constants.MILLISECONDS_PER_TIC
import com.pedrorau.featurefallingletters.presentation.main.model.FallingLetter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FallingLettersScreen(
    onBackPressed: () -> Unit
) {

    BackHandler {
        onBackPressed()
    }

    var inputText by remember { mutableStateOf("") }

    val fallingLetters = remember { mutableStateListOf<FallingLetter>() }

    val letterIdCounter = remember { mutableIntStateOf(0) }

    // Coroutine for animation
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // Box dimensions
    var containerWidth by remember { mutableFloatStateOf(0f) }
    var containerHeight by remember { mutableFloatStateOf(0f) }

    // Grid dimensions
    val gridColumns = MAX_COLUMNS_NUMBER
    var gridRows by remember { mutableIntStateOf(MAX_ROWS_NUMBER) }

    // Cell dimension (calculated)
    var cellSize by remember { mutableFloatStateOf(0f) }

    // Matrix to represent the grid occupancy status
    val gridOccupancy = remember {
        mutableStateMapOf<Pair<Int, Int>, Boolean>()
    }

    fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(android.view.View(context).windowToken, 0)
        focusManager.clearFocus()
    }

    fun isPositionOccupied(x: Int, y: Int): Boolean {
        return gridOccupancy[Pair(x, y)] == true
    }

    fun findFirstEmptyRow(column: Int): Int {
        for (row in 0 until gridRows) {
            if (isPositionOccupied(column, row)) {
                return maxOf(0, row - 1)
            }
        }
        // If none are occupied, use the last row
        return gridRows - 1
    }

    fun updateGridOccupancy() {
        gridOccupancy.clear()

        fallingLetters.forEach { letter ->
            gridOccupancy[Pair(letter.gridX, letter.gridY)] = true
        }
    }

    fun animateLetters() {
        hideKeyboard()

        coroutineScope.launch {
            val limitedText = inputText.take(MAX_CHARS_TO_TAKE)

            updateGridOccupancy()

            // Calculate the final positions for each letter
            val finalPositions = limitedText.mapIndexed { index, _ ->
                findFirstEmptyRow(index)
            }

            val lettersToAdd = limitedText.mapIndexed { index, char ->
                FallingLetter(
                    char = char,
                    gridX = index,
                    id = letterIdCounter.intValue++
                )
            }

            fallingLetters.addAll(lettersToAdd)

            val maxRowToAnimate = finalPositions.maxOrNull() ?: 0

            for (row in 1..maxRowToAnimate) {
                delay(MILLISECONDS_PER_TIC) // 0.5 seconds per tic

                // Update the Y position of all letters added in this batch
                fallingLetters.replaceAll { letter ->
                    if (lettersToAdd.any { it.id == letter.id }) {
                        val finalRow = finalPositions[lettersToAdd.indexOfFirst { it.id == letter.id }]
                        // Only move to the final row calculated for this letter
                        val newRow = minOf(row, finalRow)
                        letter.copy(gridY = newRow)
                    } else {
                        letter
                    }
                }
            }

            updateGridOccupancy()

            inputText = ""
        }
    }

    @Composable
    fun GridLines() {
        // Draw vertical lines
        for (i in 0..gridColumns) {
            Box(modifier = Modifier
                .offset(x = with(LocalDensity.current) { (i * cellSize).toDp() }, y = 0.dp)
                .width(1.dp)
                .height(with(LocalDensity.current) { (gridRows * cellSize).toDp() })
                .background(Color.LightGray)
            )
        }

        // Draw horizontal lines
        for (i in 0..gridRows) {
            Box(modifier = Modifier
                .offset(x = 0.dp, y = with(LocalDensity.current) { (i * cellSize).toDp() })
                .height(1.dp)
                .width(with(LocalDensity.current) { (gridColumns * cellSize).toDp() })
                .background(Color.LightGray)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Container for falling letters
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFF5F5F5))
                .clipToBounds()
                .onGloballyPositioned { coordinates ->
                    containerWidth = coordinates.size.width.toFloat()
                    containerHeight = coordinates.size.height.toFloat()

                    // Calculate max complete rows on container
                    val maxCellSize = containerWidth / gridColumns

                    gridRows = MAX_ROWS_NUMBER

                    //Recalculate cell dimensions to fit exactly
                    cellSize = minOf(
                        maxCellSize,
                        containerHeight / gridRows
                    )
                }
        ) {
            Box(
                //modifier = Modifier.matchParentSize()
                modifier = Modifier
                    .width(with(LocalDensity.current) { (cellSize * gridColumns).toDp() })
                    .height(with(LocalDensity.current) { (cellSize * gridRows).toDp() })
                    .align(Alignment.Center)
            ) {
                GridLines()

                fallingLetters.forEach { letter ->
                    FallingLetterDisplay(
                        letter = letter,
                        cellSize = cellSize
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(INPUT_PLACEHOLDER) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Gray,
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (inputText.isNotEmpty()) {
                        animateLetters()
                    }
                }
            ) {
                Text(BUTTON_TEXT)
            }
        }
    }
}

@Composable
fun FallingLetterDisplay(
    letter: FallingLetter,
    cellSize: Float
) {
    // Calculate position in pixels based on the grid
    val xPosition = letter.gridX * cellSize
    val yPosition = letter.gridY * cellSize

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offset(
                x = with(LocalDensity.current) { xPosition.toDp() },
                y = with(LocalDensity.current) { yPosition.toDp() }
            )
            .size(with(LocalDensity.current) { cellSize.toDp() })
            .background(Color(0xFFADD8E6))  // Falling letter background (light blue)
    ) {
        Text(
            text = letter.char.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}