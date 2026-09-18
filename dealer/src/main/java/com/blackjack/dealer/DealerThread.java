package com.blackjack.dealer;

public class DealerThread extends Thread {

    private final Dealer dealer;
    private final Deck deck;

    public DealerThread(Dealer dealer, Deck deck) {
        this.dealer = dealer;
        this.deck = deck;
    }

    @Override
    public void run() {
        System.out.println("Dealer Thread iniciado.");

        dealer.playTurn(deck);

        System.out.println("Dealer Thread terminado.");
    }
}