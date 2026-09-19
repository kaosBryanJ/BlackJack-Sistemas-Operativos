package com.blackjack.dealer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DealerApplication {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Preguntar cuántos jugadores participarán
        System.out.print("¿Cuántos jugadores participarán? ");
        int numberOfPlayers = scanner.nextInt();

        // Crear una nueva partida
        Game game = new Game();

        Deck deck = game.getDeck();
        Dealer dealer = game.getDealer();

        // Crear los jugadores
        List<Player> players = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player("Jugador " + i));
        }

        System.out.println();
        System.out.println("Cartas iniciales en la baraja: "
                + deck.remainingCards());

        // Repartir dos cartas a cada jugador
        for (Player player : players) {
            game.dealInitialCards(player);
        }

        // Repartir dos cartas al Dealer
        game.dealInitialDealerCards();

        // Mostrar estado inicial
        System.out.println();
        System.out.println("===== ESTADO INICIAL =====");

        for (Player player : players) {
            System.out.println(
                player.getName() + ": " + player.getHand()
            );
        }

        System.out.println(
            "Dealer: " + dealer.getHand()
        );

        System.out.println(
            "Cartas restantes: " + deck.remainingCards()
        );

        // Crear los hilos de los jugadores
        List<PlayerThread> playerThreads = new ArrayList<>();

        for (Player player : players) {
            playerThreads.add(
                new PlayerThread(player, deck)
            );
        }

        // Crear el hilo del Dealer
        DealerThread dealerThread =
                new DealerThread(dealer, deck);

        // Iniciar los hilos
        System.out.println();
        System.out.println("Iniciando los hilos...");

        for (PlayerThread playerThread : playerThreads) {
            playerThread.start();
        }

        dealerThread.start();

        // Esperar a que terminen los jugadores
        try {

            for (PlayerThread playerThread : playerThreads) {
                playerThread.join();
            }

            // Esperar al Dealer
            dealerThread.join();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                "Uno de los hilos fue interrumpido."
            );
        }

        // Mostrar resultados de los jugadores
        System.out.println();
        System.out.println("===== RESULTADOS DE LOS JUGADORES =====");

        for (Player player : players) {

            System.out.println();
            System.out.println(player.getName());
            System.out.println("Mano: " + player.getHand());
            System.out.println("Valor: " + player.getValue());
            System.out.println(
                "¿Blackjack?: " + player.hasBlackjack()
            );
            System.out.println(
                "¿Bust?: " + player.isBust()
            );
        }

        // Mostrar resultado del Dealer
        System.out.println();
        System.out.println("===== RESULTADO DEL DEALER =====");
        System.out.println("Mano: " + dealer.getHand());
        System.out.println("Valor: " + dealer.getValue());
        System.out.println(
            "¿Blackjack?: " + dealer.hasBlackjack()
        );
        System.out.println(
            "¿Bust?: " + dealer.isBust()
        );

        System.out.println();
        System.out.println(
            "Cartas restantes: " + deck.remainingCards()
        );

        scanner.close();
    }
}