package com.blackjack.dealer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerConnectionHandler extends Thread {

    private final Socket playerSocket;

    public PlayerConnectionHandler(Socket playerSocket) {
        this.playerSocket = playerSocket;
    }

    @Override
    public void run() {

        System.out.println(
            "[" + getName() + "] Atendiendo jugador: "
            + playerSocket.getRemoteSocketAddress()
        );

        try (
            Socket socket = playerSocket;

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

            String playerName = input.readLine();

            if (playerName == null) {
                System.out.println(
                    "[" + getName()
                    + "] El jugador se desconectó sin enviar nombre."
                );
                return;
            }

            System.out.println(
                "[" + getName()
                + "] Nombre recibido: "
                + playerName
            );

            output.println(
                "Bienvenido "
                + playerName
                + ". Conexión con el Dealer establecida."
            );

            System.out.println(
                "[" + getName()
                + "] Comunicación finalizada con "
                + playerName
            );

        } catch (IOException e) {

            System.out.println(
                "[" + getName()
                + "] Error con el jugador: "
                + e.getMessage()
            );
        }
    }
}