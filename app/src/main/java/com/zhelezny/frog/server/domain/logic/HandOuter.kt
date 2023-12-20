package com.zhelezny.frog.server.domain.logic

import com.zhelezny.frog.data.Player

class HandOuter(private var sessionPlayers: /*List<Player>*/Player) : HandOuterInterface {

    var cardsDeck = arrayListOf(
        "+b", "+b", "+b", "+b", "+b", "++b", "-b", "-b",
        "+g", "+g", "+g", "+g", "+g", "++g", "-g", "-g",
        "+r", "+r", "+r", "+r", "+r", "++r", "-r", "-r",
        "+p", "+p", "+p", "+p", "+p", "++p", "-p", "-p",
        "+y", "+y", "+y", "+y", "+y", "++y", "-y", "-y",
        "|", "|", "|", "||", "||", "+", "+", "+", "+", "+", "-", "-"
    )

    override fun handOut(/*playerCards: List<Int>*/) {
        val cardsOfPlayer = mutableListOf<String>()
        repeat(5) {
            cardsOfPlayer.add(random())
        }
        sessionPlayers.playerCards = cardsOfPlayer

    }

    var currentCards = cardsDeck.random()
//    fun getStartCards(): String {
//        currentCards.take(5)
//    }

    private fun random(): String {
        val countCardOfDeck = 52
        val indexCard = (0..countCardOfDeck).random()
        val card = cardsDeck[indexCard]
        cardsDeck.remove(card)
        return card
    }
}