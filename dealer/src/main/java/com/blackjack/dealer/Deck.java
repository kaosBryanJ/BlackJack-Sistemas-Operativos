package com.blackjack.dealer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        createDeck();
    }

    private void createDeck() {
        String[] suits = {
            "Corazones",
            "Diamantes",
            "Tréboles",
            "Picas"
        };

        String[] ranks = {
            "A", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "J", "Q", "K"
        };

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("La baraja no tiene más cartas.");
        }

        return cards.remove(cards.size() - 1);
    }

    public int remainingCards() {
        return cards.size();
    }
}