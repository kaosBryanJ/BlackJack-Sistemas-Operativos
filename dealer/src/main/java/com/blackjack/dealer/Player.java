package com.blackjack.dealer;

public class Player {

    private final String name;
    private final Hand hand;
    private final PlayerStrategy strategy;

    // Constructor por defecto: usa estrategia Normal
    public Player(String name) {
        this(name, new StandardStrategy());
    }

    // Constructor que permite elegir una estrategia
    public Player(String name, PlayerStrategy strategy) {
        this.name = name;
        this.hand = new Hand();
        this.strategy = strategy;
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

    public String getStrategyName() {
        return strategy.getName();
    }

    public boolean shouldHit() {
        return strategy.shouldHit(hand);
    }

    public void playTurn(Deck deck) {

        System.out.println(
            name + " utiliza estrategia: "
            + strategy.getName()
        );

        while (shouldHit()) {

            Card card = deck.drawCard();
            receiveCard(card);

            System.out.println(
                name + " toma carta: " + card
            );

            System.out.println(
                "Valor actual de "
                + name + ": " + getValue()
            );

            if (isBust()) {
                System.out.println(
                    name + " se pasa con: "
                    + getValue()
                );
                return;
            }
        }

        System.out.println(
            name + " se planta con: "
            + getValue()
        );
    }
}