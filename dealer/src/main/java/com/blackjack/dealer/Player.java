package com.blackjack.dealer;

public class Player {

    private final String name;
    private final Hand hand;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public int getValue() {
        return hand.getValue();
    }

    public boolean isBust() {
        return hand.isBust();
    }

    public boolean hasBlackjack() {
        return hand.isBlackjack();
    }

    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public boolean shouldHit() {
        return getValue() < 17;
    }

    public void playTurn(Deck deck) {

        while (shouldHit()) {

            Card card = deck.drawCard();
            receiveCard(card);

            System.out.println(
                name + " toma carta: " + card
            );

            System.out.println(
                "Valor actual de " + name + ": " + getValue()
            );

            if (isBust()) {
                System.out.println(
                    name + " se pasa con: " + getValue()
                );
            }
        }

        if (!isBust()) {
            System.out.println(
                name + " se planta con: " + getValue()
            );
        }
    }
}