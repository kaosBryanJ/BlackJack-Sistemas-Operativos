package com.blackjack.dealer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DealerServer {

    private static final int PORT = 5000;

    public static void main(String[] args) {

        int playerCount = readPlayerCount(args);

        System.out.println(
            "================================"
        );

        System.out.println(
            "       BLACKJACK DEALER"
        );

        System.out.println(
            "================================"
        );

        System.out.println(
            "Jugadores configurados: "
            + playerCount
        );

        try (
            ServerSocket serverSocket =
                new ServerSocket(PORT)
        ) {

            int roundNumber = 1;

            while (true) {

                System.out.println();
                System.out.println(
                    "================================"
                );
                System.out.println(
                    " ESPERANDO PARTIDA "
                    + roundNumber
                );
                System.out.println(
                    "================================"
                );

                GameRound round =
                    new GameRound(
                        playerCount,
                        roundNumber
                    );

                // ======================================
                // ACEPTAR LOS JUGADORES
                // ======================================

                for (
                    int i = 0;
                    i < playerCount;
                    i++
                ) {

                    Socket playerSocket =
                        serverSocket.accept();

                    System.out.println(
                        "Nueva conexion desde: "
                        + playerSocket.getRemoteSocketAddress()
                    );

                    PlayerConnectionHandler handler =
                        new PlayerConnectionHandler(
                            playerSocket,
                            round,
                            i
                        );

                    handler.start();
                }

                // ======================================
                // ESPERAR A QUE TODOS ENVIEN SU NOMBRE
                // ======================================

                round.awaitAllPlayersReady();

                System.out.println();
                System.out.println(
                    "Todos los jugadores estan conectados."
                );

                // ======================================
                // INICIAR PARTIDA
                // ======================================

                BlackjackGameThread gameThread =
                    new BlackjackGameThread(round);

                gameThread.start();

                gameThread.join();

                System.out.println(
                    "El servidor esta listo para la siguiente partida."
                );

                roundNumber++;
            }

        } catch (IOException e) {

            System.out.println(
                "Error en el servidor del Dealer: "
                + e.getMessage()
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                "El servidor fue interrumpido."
            );
        }
    }

    private static int readPlayerCount(
        String[] args
    ) {

        String value = null;

        if (
            args != null
            && args.length > 0
        ) {

            value = args[0];

        } else {

            value =
                System.getenv(
                    "BLACKJACK_PLAYERS"
                );
        }

        if (
            value == null
            || value.isBlank()
        ) {

            return 2;
        }

        try {

            int count =
                Integer.parseInt(value);

            if (count < 1) {
                return 2;
            }

            return count;

        } catch (NumberFormatException e) {

            return 2;
        }
    }
}