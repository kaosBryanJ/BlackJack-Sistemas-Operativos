package com.blackjack.dealer;

public class DealerApplication {

    public static void main(String[] args) {

        // Crear una nueva partida
        Game game = new Game();

        Deck deck = game.getDeck();
        Dealer dealer = game.getDealer();

        Player player = new Player("Jugador 1");

        System.out.println("Cartas iniciales en la baraja: "
                + deck.remainingCards());

        // Repartir las cartas iniciales
        game.dealInitialCards(player);

        // Mostrar estado inicial
        game.showInitialState(player);

        // Crear los hilos
        DealerThread dealerThread = new DealerThread(dealer, deck);
        PlayerThread playerThread = new PlayerThread(player, deck);

        // Iniciar ambos hilos
        System.out.println();
        System.out.println("Iniciando los hilos...");

        playerThread.start();
        dealerThread.start();

        // Esperar a que ambos hilos terminen
        try {
            playerThread.join();
            dealerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Uno de los hilos fue interrumpido.");
        }

        // Resultado del Jugador
        System.out.println();
        System.out.println("===== RESULTADO DEL JUGADOR =====");
        System.out.println("Mano: " + player.getHand());
        System.out.println("Valor: " + player.getValue());
        System.out.println("¿Blackjack?: " + player.hasBlackjack());
        System.out.println("¿Bust?: " + player.isBust());

        // Resultado del Dealer
        System.out.println();
        System.out.println("===== RESULTADO DEL DEALER =====");
        System.out.println("Mano: " + dealer.getHand());
        System.out.println("Valor: " + dealer.getValue());
        System.out.println("¿Blackjack?: " + dealer.hasBlackjack());
        System.out.println("¿Bust?: " + dealer.isBust());

        System.out.println();
        System.out.println("Cartas restantes: " + deck.remainingCards());
    }
}