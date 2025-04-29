package com.pedrorau.featurefallingletters.presentation.main.model

data class FallingLetter(
    val char: Char,
    val gridX: Int,  // position X on grid
    val id: Int,
    var gridY: Int = 0  // position Y on grid
)