package com.blackjack.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerClient {

    private static final String DEALER_HOST = "localhost";
    private static final int DEALER_PORT = 5000;

    public static void main(String[] args) {

        String playerName = "Jugador";

        if (args.length > 0) {
            playerName = args[0];
        }

        System.out.println("================================");
        System.out.println("       BLACKJACK PLAYER");
        System.out.println("================================");

        try (
            Socket socket = new Socket(
                DEALER_HOST,
                DEALER_PORT
            );

            BufferedReader input = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()
                )
            );

            PrintWriter output = new PrintWriter(
                socket.getOutputStream(),
                true
            )
        ) {

            System.out.println(
                "Conexion exitosa con el Dealer en "
                + DEALER_HOST
                + ":"
                + DEALER_PORT
            );

            output.println(playerName);

            System.out.println(
                "Nombre enviado al Dealer: "
                + playerName
            );

            String dealerResponse = input.readLine();

            System.out.println(
                "Respuesta del Dealer: "
                + dealerResponse
            );

        } catch (IOException e) {

            System.out.println(
                "No fue posible conectar con el Dealer: "
                + e.getMessage()
            );
        }
    }
}