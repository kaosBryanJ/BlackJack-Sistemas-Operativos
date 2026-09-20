package com.blackjack.dealer;

public class GameThread extends Thread {

    private Game game;

    @Override
    public void run() {

        System.out.println("Game Thread iniciado.");

        game = new Game();

        System.out.println("Partida y baraja inicializadas.");
        System.out.println("Game Thread terminado.");
    }

    public Game getGame() {
        return game;
    }
}