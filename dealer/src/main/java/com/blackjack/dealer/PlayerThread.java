package com.blackjack.dealer;

public class PlayerThread extends Thread {

    private final Player player;
    private final Deck deck;

    public PlayerThread(Player player, Deck deck) {
        this.player = player;
        this.deck = deck;
    }

    @Override
    public void run() {
        System.out.println(
            "Hilo de " + player.getName() + " iniciado."
        );

        player.playTurn(deck);

        System.out.println(
            "Hilo de " + player.getName() + " terminado."
        );
    }
}