package com.blackjack.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerClient {

    private static final String DEALER_HOST =
        getDealerHost();

    private static final int DEALER_PORT = 5000;

    public static void main(String[] args) {

        String playerName =
            getPlayerName(args);

        System.out.println(
            "================================"
        );

        System.out.println(
            "       BLACKJACK PLAYER"
        );

        System.out.println(
            "================================"
        );

        try (
            Socket socket =
                new Socket(
                    DEALER_HOST,
                    DEALER_PORT
                );

            BufferedReader input =
                new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()
                    )
                );

            PrintWriter output =
                new PrintWriter(
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

            String dealerResponse;

            while (
                (dealerResponse =
                    input.readLine()) != null
            ) {

                System.out.println();

                System.out.println(
                    dealerResponse
                );

                if (
                    dealerResponse.equals(
                        "=== FIN DE PARTIDA ==="
                    )
                ) {

                    break;
                }
            }

            System.out.println();

            System.out.println(
                "===== CONEXION FINALIZADA ====="
            );

        } catch (IOException e) {

            System.out.println(
                "No fue posible conectar con el Dealer: "
                + e.getMessage()
            );
        }
    }

    private static String getDealerHost() {

        String host =
            System.getenv(
                "BLACKJACK_DEALER_HOST"
            );

        if (
            host == null
            || host.isBlank()
        ) {

            return "localhost";
        }

        return host;
    }

    private static String getPlayerName(
        String[] args
    ) {

        if (
            args != null
            && args.length > 0
            && !args[0].isBlank()
        ) {

            return args[0];
        }

        String name =
            System.getenv(
                "BLACKJACK_PLAYER_NAME"
            );

        if (
            name == null
            || name.isBlank()
        ) {

            return "Jugador 1";
        }

        return name;
    }
}