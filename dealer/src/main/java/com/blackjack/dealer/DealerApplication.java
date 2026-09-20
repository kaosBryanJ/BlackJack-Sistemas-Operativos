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

            // Hilo encargado de inicializar la partida y la baraja
            GameThread gameThread = new GameThread();
            gameThread.start();

            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(
                    "El hilo de inicialización fue interrumpido."
                );
                break;
            }

            Game game = gameThread.getGame();

            Deck deck = game.getDeck();
            Dealer dealer = game.getDealer();

            // Crear jugadores con diferentes estrategias
            List<Player> players = new ArrayList<>();

            for (int i = 1; i <= numberOfPlayers; i++) {

                PlayerStrategy strategy;

                switch ((i - 1) % 3) {

                    case 0:
                        strategy = new ConservativeStrategy();
                        break;

                    case 1:
                        strategy = new StandardStrategy();
                        break;

                    default:
                        strategy = new AggressiveStrategy();
                        break;
                }

                players.add(
                    new Player(
                        "Jugador " + i,
                        strategy
                    )
                );
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
                    player.getName()
                    + ": "
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

            // Crear hilos de jugadores
            List<PlayerThread> playerThreads =
                new ArrayList<>();

            for (Player player : players) {

                playerThreads.add(
                    new PlayerThread(player, deck)
                );
            }

            // Crear hilo del Dealer
            DealerThread dealerThread =
                new DealerThread(dealer, deck);

            System.out.println();
            System.out.println("Iniciando los hilos...");

            // Iniciar jugadores
            for (PlayerThread playerThread : playerThreads) {
                playerThread.start();
            }

            // Iniciar Dealer
            dealerThread.start();

            // Esperar a que todos los hilos terminen
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

            // Resultados de jugadores
            System.out.println();
            System.out.println(
                "===== RESULTADOS DE LOS JUGADORES ====="
            );

            for (Player player : players) {

                System.out.println();
                System.out.println(player.getName());

                System.out.println(
                    "Estrategia: "
                    + player.getStrategyName()
                );

                System.out.println(
                    "Mano: "
                    + player.getHand()
                );

                System.out.println(
                    "Valor: "
                    + player.getValue()
                );

                System.out.println(
                    "¿Blackjack?: "
                    + player.hasBlackjack()
                );

                System.out.println(
                    "¿Bust?: "
                    + player.isBust()
                );

                System.out.println(
                    "Resultado: "
                    + game.determineResult(player)
                );
            }

            // Resultado del Dealer
            System.out.println();
            System.out.println(
                "===== RESULTADO DEL DEALER ====="
            );

            System.out.println(
                "Mano: "
                + dealer.getHand()
            );

            System.out.println(
                "Valor: "
                + dealer.getValue()
            );

            System.out.println(
                "¿Blackjack?: "
                + dealer.hasBlackjack()
            );

            System.out.println(
                "¿Bust?: "
                + dealer.isBust()
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