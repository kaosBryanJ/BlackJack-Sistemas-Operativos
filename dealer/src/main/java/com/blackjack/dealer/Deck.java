package com.blackjack.dealer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> cards;
    private final DeckSemaphore deckSemaphore;

    public Deck() {
        cards = new ArrayList<>();
        deckSemaphore = new DeckSemaphore();
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

        try {
            deckSemaphore.acquire();

            if (cards.isEmpty()) {
                throw new IllegalStateException("La baraja no tiene más cartas.");
            }

            return cards.remove(cards.size() - 1);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("El hilo fue interrumpido al intentar tomar una carta.", e);

        } finally {
            deckSemaphore.release();
        }
    }

    public int remainingCards() {
        return cards.size();
    }
}