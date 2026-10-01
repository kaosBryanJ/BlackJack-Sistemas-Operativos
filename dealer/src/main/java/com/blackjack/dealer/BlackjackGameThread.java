package com.blackjack.dealer;

public class BlackjackGameThread extends Thread {

    private final GameRound round;

    public BlackjackGameThread(GameRound round) {
        this.round = round;
        setName("Hilo-Coordinador");
    }

    @Override
    public void run() {

        try {

            // ============================================
            // 1. HILO INICIALIZADOR
            // ============================================

            GameInitializer initializer =
                new GameInitializer(round);

            initializer.start();
            initializer.join();

            // ============================================
            // 2. HILO DEL CRUPIER
            // ============================================

            BlackjackDealerThread dealerThread =
                new BlackjackDealerThread(round);

            dealerThread.start();

            // ============================================
            // 3. LIBERAR A LOS HILOS DE LOS JUGADORES
            // ============================================

            round.startPlayers();

            // ============================================
            // 4. ESPERAR A TODOS LOS JUGADORES
            // ============================================

            for (
                PlayerConnectionHandler handler
                : round.getHandlers()
            ) {

                handler.join();
            }

            // ============================================
            // 5. ESPERAR AL CRUPIER
            // ============================================

            dealerThread.join();

            System.out.println();
            System.out.println(
                "================================"
            );
            System.out.println(
                " PARTIDA "
                + round.getRoundNumber()
                + " FINALIZADA"
            );
            System.out.println(
                "================================"
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                "[Hilo-Coordinador] Interrumpido."
            );
        }
    }
}