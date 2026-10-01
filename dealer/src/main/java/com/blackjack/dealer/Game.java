package com.blackjack.dealer;

public class Game {

    private final Deck deck;
    private final Dealer dealer;

    public Game() {

        deck = new Deck();
        dealer = new Dealer();

        System.out.println(
            "Baraja creada y mezclada."
        );
    }

    public Deck getDeck() {
        return deck;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void dealInitialCards(Player player) {

        System.out.println(
            "[" + player.getName()
            + "] recibiendo cartas iniciales."
        );

        player.receiveCard(deck.drawCard());
        player.receiveCard(deck.drawCard());
    }

    public void dealInitialDealerCards() {

        System.out.println(
            "[Crupier] recibiendo cartas iniciales."
        );

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

    public String determineResult(Player player) {

        if (player.isBust()) {
            return "PERDI\u00d3";
        }

        if (dealer.isBust()) {
            return "GAN\u00d3";
        }

        if (player.hasBlackjack() && dealer.hasBlackjack()) {
            return "EMPATE";
        }

        if (player.hasBlackjack()) {
            return "GAN\u00d3";
        }

        if (dealer.hasBlackjack()) {
            return "PERDI\u00d3";
        }

        if (player.getValue() > dealer.getValue()) {
            return "GAN\u00d3";
        }

        if (player.getValue() < dealer.getValue()) {
            return "PERDI\u00d3";
        }

        return "EMPATE";
    }
}