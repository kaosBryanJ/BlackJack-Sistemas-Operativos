package com.blackjack.dealer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DealerApplication {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("¿Cuántos jugadores participarán? ");
        int numberOfPlayers = scanner.nextInt();

        boolean playAgain = true;
        int gameNumber = 1;

        while (playAgain) {

            System.out.println();
            System.out.println("================================");
            System.out.println("          PARTIDA " + gameNumber);
            System.out.println("================================");

            // Crear el hilo encargado de inicializar la partida
            GameThread gameThread = new GameThread();

            gameThread.start();

            // Esperar a que termine la inicialización
            try {

                gameThread.join();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                System.out.println(
                    "El hilo de inicialización fue interrumpido."
                );

                return;
            }

            // Obtener la partida creada por el Game Thread
            Game game = gameThread.getGame();

            Deck deck = game.getDeck();
            Dealer dealer = game.getDealer();

            // Crear los jugadores
            List<Player> players = new ArrayList<>();

            for (int i = 1; i <= numberOfPlayers; i++) {
                players.add(new Player("Jugador " + i));
            }

            System.out.println();
            System.out.println(
                "Cartas iniciales en la baraja: "
                + deck.remainingCards()
            );

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
                    player.getName() + ": "
                    + player.getHand()
                );
            }

            System.out.println(
                "Dealer: " + dealer.getHand()
            );

            System.out.println(
                "Cartas restantes: "
                + deck.remainingCards()
            );

            // Crear los hilos de los jugadores
            List<PlayerThread> playerThreads =
                    new ArrayList<>();

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

            // Esperar a que terminen los hilos
            try {

                for (PlayerThread playerThread : playerThreads) {
                    playerThread.join();
                }

                dealerThread.join();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                System.out.println(
                    "Uno de los hilos fue interrumpido."
                );
            }

            // Mostrar resultados de los jugadores
            System.out.println();
            System.out.println(
                "===== RESULTADOS DE LOS JUGADORES ====="
            );

            for (Player player : players) {

                System.out.println();
                System.out.println(player.getName());
                System.out.println(
                    "Mano: " + player.getHand()
                );
                System.out.println(
                    "Valor: " + player.getValue()
                );
                System.out.println(
                    "¿Blackjack?: "
                    + player.hasBlackjack()
                );
                System.out.println(
                    "¿Bust?: " + player.isBust()
                );
            }

            // Mostrar resultado del Dealer
            System.out.println();
            System.out.println(
                "===== RESULTADO DEL DEALER ====="
            );
            System.out.println(
                "Mano: " + dealer.getHand()
            );
            System.out.println(
                "Valor: " + dealer.getValue()
            );
            System.out.println(
                "¿Blackjack?: "
                + dealer.hasBlackjack()
            );
            System.out.println(
                "¿Bust?: " + dealer.isBust()
            );

            System.out.println();
            System.out.println(
                "Cartas restantes: "
                + deck.remainingCards()
            );

            // Preguntar si se desea jugar otra partida
            System.out.println();
            System.out.print(
                "¿Desea jugar otra partida? (s/n): "
            );

            String answer = scanner.next();

            if (answer.equalsIgnoreCase("s")) {
                gameNumber++;
            } else {
                playAgain = false;
            }
        }

        System.out.println();
        System.out.println("================================");
        System.out.println("       JUEGO FINALIZADO");
        System.out.println("================================");

        scanner.close();
    }
}