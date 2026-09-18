package com.blackjack.dealer;

public class DealerApplication {

    public static void main(String[] args) {

        Deck deck = new Deck();
        deck.shuffle();

        Dealer dealer = new Dealer();

        System.out.println("Cartas iniciales: " + deck.remainingCards());

        dealer.receiveCard(new Card("5", "Corazones"));
        dealer.receiveCard(new Card("6", "Picas"));
        
        dealer.playTurn(deck);

        System.out.println("Mano del Dealer: " + dealer.getHand());
        System.out.println("Valor del Dealer: " + dealer.getValue());
        System.out.println("¿Debe pedir carta?: " + dealer.shouldHit());
        System.out.println("¿Blackjack?: " + dealer.hasBlackjack());
        System.out.println("¿Bust?: " + dealer.isBust());

        System.out.println("Cartas restantes: " + deck.remainingCards());
    }
}