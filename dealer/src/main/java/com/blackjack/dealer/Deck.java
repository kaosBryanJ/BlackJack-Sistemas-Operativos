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
        shuffle();
    }

    private void createDeck() {

        String[] suits = {
            "Corazones",
            "Diamantes",
            "Treboles",
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

        String actor = Thread.currentThread().getName();

        boolean acquired = false;

        System.out.println(
            "[" + actor + "] esperando acceso a la baraja..."
        );

        try {

            deckSemaphore.acquire();
            acquired = true;

            System.out.println(
                "[" + actor + "] obtuvo acceso a la baraja."
            );

            if (cards.isEmpty()) {
                throw new IllegalStateException(
                    "La baraja no tiene mas cartas."
                );
            }

            Card card = cards.remove(cards.size() - 1);

            System.out.println(
                "[" + actor + "] tomo: " + card
            );

            return card;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                "El hilo fue interrumpido al intentar tomar una carta.",
                e
            );

        } finally {

            if (acquired) {

                deckSemaphore.release();

                System.out.println(
                    "[" + actor + "] libero la baraja."
                );
            }
        }
    }

    public int remainingCards() {
        return cards.size();
    }
}