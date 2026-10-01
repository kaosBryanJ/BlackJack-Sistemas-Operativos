package com.blackjack.dealer;

import java.util.List;

public class BlackjackDealerThread extends Thread {

    private final GameRound round;

    public BlackjackDealerThread(GameRound round) {
        this.round = round;
        setName("Hilo-Crupier");
    }

    @Override
    public void run() {

        try {

            round.awaitAllPlayerTurns();

            Game game = round.getGame();

            System.out.println();
            System.out.println(
                "================================"
            );
            System.out.println(
                "       TURNO DEL CRUPIER"
            );
            System.out.println(
                "================================"
            );

            game.getDealer().playTurn(
                game.getDeck()
            );

            List<Player> players = round.getPlayers();
            List<PlayerConnectionHandler> handlers =
                round.getHandlers();

            for (int i = 0; i < players.size(); i++) {

                Player player = players.get(i);
                PlayerConnectionHandler handler =
                    handlers.get(i);

                String result =
                    game.determineResult(player);

                handler.sendLine(
                    "=== INFORMACION FINAL DEL CRUPIER ==="
                );

                handler.sendLine(
                    "Mano final del Dealer: "
                    + game.getDealer().getHand()
                );

                handler.sendLine(
                    "Valor del Dealer: "
                    + game.getDealer().getValue()
                );

                handler.sendLine(
                    "Dealer Bust: "
                    + game.getDealer().isBust()
                );

                handler.sendLine(
                    "RESULTADO: "
                    + result
                );

                handler.sendLine(
                    "=== FIN DE PARTIDA ==="
                );

                System.out.println(
                    "[Hilo-Crupier] Resultado para "
                    + player.getName()
                    + ": "
                    + result
                );
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                "[Hilo-Crupier] Interrumpido."
            );

        } finally {

            round.publishResults();
        }
    }
}