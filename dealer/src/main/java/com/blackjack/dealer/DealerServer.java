package com.blackjack.dealer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

                try (
                    Socket playerSocket = serverSocket.accept();

                    BufferedReader input = new BufferedReader(
                        new InputStreamReader(
                            playerSocket.getInputStream()
                        )
                    );

                    PrintWriter output = new PrintWriter(
                        playerSocket.getOutputStream(),
                        true
                    )
                ) {

                    System.out.println(
                        "Jugador conectado desde: "
                        + playerSocket.getRemoteSocketAddress()
                    );

                    String playerName = input.readLine();

                    System.out.println(
                        "Nombre recibido: "
                        + playerName
                    );

                    output.println(
                        "Bienvenido "
                        + playerName
                        + ". Conexión con el Dealer establecida."
                    );
                }
            }

        } catch (IOException e) {

            System.out.println(
                "Error en el servidor del Dealer: "
                + e.getMessage()
            );
        }
    }
}