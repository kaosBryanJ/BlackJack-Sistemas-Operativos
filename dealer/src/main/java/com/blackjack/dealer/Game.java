package com.blackjack.dealer;

public class Game {

    private final Deck deck;
    private final Dealer dealer;

    public Game() {
        deck = new Deck();
        dealer = new Dealer();

        deck.shuffle();
    }

    public Deck getDeck() {
        return deck;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void dealInitialCards(Player player) {

        // Dos cartas para el jugador
        player.receiveCard(deck.drawCard());
        player.receiveCard(deck.drawCard());

        // Dos cartas para el Dealer
        dealer.receiveCard(deck.drawCard());
        dealer.receiveCard(deck.drawCard());
    }

    public void showInitialState(Player player) {

        System.out.println();
        System.out.println("===== ESTADO INICIAL =====");

        System.out.println(
            player.getName() + ": " + player.getHand()
        );

        System.out.println(
            "Dealer: " + dealer.getHand()
        );

        System.out.println(
            "Cartas restantes: " + deck.remainingCards()
        );
    }
}