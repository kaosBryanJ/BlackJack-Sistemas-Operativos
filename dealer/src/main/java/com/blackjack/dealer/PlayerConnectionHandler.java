package com.blackjack.dealer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerConnectionHandler extends Thread {

    private final Socket playerSocket;
    private final GameRound round;
    private final int playerIndex;

    private BufferedReader input;
    private PrintWriter output;

    private Player player;

    public PlayerConnectionHandler(
        Socket playerSocket,
        GameRound round,
        int playerIndex
    ) {

        this.playerSocket = playerSocket;
        this.round = round;
        this.playerIndex = playerIndex;

        setName(
            "Hilo-Jugador-" + (playerIndex + 1)
        );
    }

    // Constructor compatible con el codigo anterior.
    public PlayerConnectionHandler(Socket playerSocket) {
        this(playerSocket, null, -1);
    }

    @Override
    public void run() {

        boolean registered = false;
        boolean turnFinished = false;

        try {

            input = new BufferedReader(
                new InputStreamReader(
                    playerSocket.getInputStream()
                )
            );

            output = new PrintWriter(
                playerSocket.getOutputStream(),
                true
            );

            String playerName = input.readLine();

            if (
                playerName == null
                || playerName.isBlank()
            ) {

                sendLine(
                    "Error: nombre de jugador no recibido."
                );

                return;
            }

            // ==========================================
            // MODO COMPATIBILIDAD
            // ==========================================

            if (round == null) {

                sendLine(
                    "Bienvenido "
                    + playerName
                    + ". Conexion con el Dealer establecida."
                );

                return;
            }

            // ==========================================
            // CREAR JUGADOR
            // ==========================================

            PlayerStrategy strategy =
                createStrategy(playerIndex);

            player =
                new Player(
                    playerName,
                    strategy
                );

            setName(
                "Hilo-"
                + playerName
            );

            registered =
                round.registerPlayer(
                    this,
                    player
                );

            if (!registered) {

                sendLine(
                    "No fue posible registrar al jugador."
                );

                return;
            }

            // ==========================================
            // CONFIRMACION
            // ==========================================

            sendLine(
                "Bienvenido "
                + playerName
                + ". Conexion con el Dealer establecida."
            );

            // ==========================================
            // ESPERAR INICIO
            // ==========================================

            round.awaitGameStart();

            Game game = round.getGame();

            sendLine(
                "=== PARTIDA "
                + round.getRoundNumber()
                + " INICIADA ==="
            );

            sendLine(
                "Jugador: "
                + player.getName()
            );

            sendLine(
                "Estrategia: "
                + player.getStrategyName()
            );

            sendLine(
                "Mano inicial: "
                + player.getHand()
            );

            sendLine(
                "Dealer: "
                + game.getDealer().getHand()
            );

            // ==========================================
            // TURNO DEL JUGADOR
            // ==========================================

            round.awaitTurn(playerIndex);

            System.out.println();
            System.out.println(
                "["
                + getName()
                + "] INICIA SU TURNO"
            );

            player.playTurn(
                game.getDeck()
            );

            sendLine(
                "Mano final del jugador: "
                + player.getHand()
            );

            sendLine(
                "Valor del jugador: "
                + player.getValue()
            );

            sendLine(
                "Jugador Bust: "
                + player.isBust()
            );

            System.out.println(
                "["
                + getName()
                + "] termino su turno."
            );

            round.finishPlayerTurn(
                playerIndex
            );

            turnFinished = true;

            // ==========================================
            // ESPERAR AL CRUPIER
            // ==========================================

            round.awaitResults();

            sendLine(
                "===== CONEXION FINALIZADA ====="
            );

        } catch (IOException e) {

            System.out.println(
                "["
                + getName()
                + "] Error de comunicacion: "
                + e.getMessage()
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                "["
                + getName()
                + "] Hilo interrumpido."
            );

        } finally {

            // Si el jugador se desconecto durante su turno,
            // liberamos el siguiente jugador para evitar bloqueo.
            if (
                registered
                && !turnFinished
                && round != null
            ) {

                try {
                    round.finishPlayerTurn(
                        playerIndex
                    );
                } catch (Exception ignored) {
                }
            }

            try {

                if (input != null) {
                    input.close();
                }

                if (output != null) {
                    output.close();
                }

                if (
                    playerSocket != null
                    && !playerSocket.isClosed()
                ) {
                    playerSocket.close();
                }

            } catch (IOException ignored) {
            }
        }
    }

    private PlayerStrategy createStrategy(
        int index
    ) {

        switch (index % 3) {

            case 0:
                return new ConservativeStrategy();

            case 1:
                return new StandardStrategy();

            default:
                return new AggressiveStrategy();
        }
    }

    public synchronized void sendLine(
        String message
    ) {

        if (output != null) {
            output.println(message);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}