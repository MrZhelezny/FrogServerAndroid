package com.zhelezny.frog.server.data

enum class Color(val colorName: String) {
    YELLOW("yellow"),
    RED("red"),
    BLUE("blue"),
    PURPLE("purple"),
    GREEN("green")
}

fun getRandomColor(countPlayers: Int): List<Color> {
    val colors = listOf(Color.BLUE, Color.GREEN, Color.PURPLE, Color.RED, Color.YELLOW)
    return colors.shuffled().take(countPlayers)
}