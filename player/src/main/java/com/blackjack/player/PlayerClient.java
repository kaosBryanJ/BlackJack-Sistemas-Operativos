package com.blackjack.player;

import java.io.IOException;
import java.net.Socket;

public class PlayerClient {

    private static final String DEALER_HOST = "localhost";
    private static final int DEALER_PORT = 5000;

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       BLACKJACK PLAYER");
        System.out.println("================================");

        try (Socket socket = new Socket(DEALER_HOST, DEALER_PORT)) {

            System.out.println(
                "Conexión exitosa con el Dealer en "
                + DEALER_HOST
                + ":"
                + DEALER_PORT
            );

        } catch (IOException e) {

            System.out.println(
                "No fue posible conectar con el Dealer: "
                + e.getMessage()
            );
        }
    }
}