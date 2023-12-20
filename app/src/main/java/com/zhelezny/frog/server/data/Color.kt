package com.zhelezny.frog.server.data

enum class Color {
    YELLOW, RED, BLUE, PURPLE, GREEN
}

fun getRandomColor(countPlayers: Int): List<Color> {
    val colors = listOf(Color.BLUE, Color.GREEN, Color.PURPLE, Color.RED, Color.YELLOW)
    return colors.shuffled().take(countPlayers)
}