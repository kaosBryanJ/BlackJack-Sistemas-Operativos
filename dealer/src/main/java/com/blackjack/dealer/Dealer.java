package com.blackjack.dealer;

public class Dealer {

    private final Hand hand;

    public Dealer() {
        hand = new Hand();
    }

    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public Hand getHand() {
        return hand;
    }

    public int getValue() {
        return hand.getValue();
    }

    public boolean shouldHit() {
        return getValue() < 17;
    }

    public boolean isBust() {
        return hand.isBust();
    }

    public boolean hasBlackjack() {
        return hand.isBlackjack();
    }

    public void playTurn(Deck deck) {

        while (shouldHit()) {

            Card card = deck.drawCard();
            receiveCard(card);

            System.out.println("Dealer toma carta: " + card);
            System.out.println("Valor actual del Dealer: " + getValue());
        }

        System.out.println("Dealer se planta con: " + getValue());
    }
}