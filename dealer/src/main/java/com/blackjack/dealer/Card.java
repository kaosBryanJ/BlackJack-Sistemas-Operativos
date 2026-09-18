package com.blackjack.dealer;

public class Card {

    private final String rank;
    private final String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        switch (rank) {
            case "A":
                return 11;
            case "J":
            case "Q":
            case "K":
                return 10;
            default:
                return Integer.parseInt(rank);
        }
    }

    @Override
    public String toString() {
        return rank + " de " + suit;
    }
}