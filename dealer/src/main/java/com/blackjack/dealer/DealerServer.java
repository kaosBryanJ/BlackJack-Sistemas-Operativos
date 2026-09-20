package com.blackjack.dealer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DealerServer {

    private static final int PORT = 5000;

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       BLACKJACK DEALER");
        System.out.println("================================");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println(
                "Dealer esperando jugadores en el puerto "
                + PORT
                + "..."
            );

            while (true) {

                Socket playerSocket = serverSocket.accept();

                System.out.println(
                    "Jugador conectado desde: "
                    + playerSocket.getRemoteSocketAddress()
                );

                playerSocket.close();
            }

        } catch (IOException e) {

            System.out.println(
                "Error en el servidor del Dealer: "
                + e.getMessage()
            );
        }
    }
}