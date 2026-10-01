package com.blackjack.dealer;

public class GameInitializer extends Thread {

    private final GameRound round;

    public GameInitializer(GameRound round) {
        this.round = round;
        setName("Hilo-Inicializador");
    }

    @Override
    public void run() {

        System.out.println();
        System.out.println("================================");
        System.out.println("      HILO INICIALIZADOR");
        System.out.println("================================");

        System.out.println(
            "Creando nueva baraja..."
        );

        Game game = new Game();

        round.setGame(game);

        System.out.println(
            "Creando jugadores..."
        );

        for (Player player : round.getPlayers()) {

            System.out.println(
                player.getName()
                + " -> estrategia: "
                + player.getStrategyName()
            );
        }

        System.out.println(
            "Creando crupier..."
        );

        System.out.println();
        System.out.println(
            "===== REPARTO INICIAL ====="
        );

        for (Player player : round.getPlayers()) {
            game.dealInitialCards(player);
        }

        game.dealInitialDealerCards();

        System.out.println();
        System.out.println(
            "Inicializacion terminada."
        );

        System.out.println(
            "Cartas restantes: "
            + game.getDeck().remainingCards()
        );
    }
}