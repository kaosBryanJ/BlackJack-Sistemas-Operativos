package com.blackjack.dealer;

public class ConservativeStrategy implements PlayerStrategy {

    @Override
    public boolean shouldHit(Hand hand) {
        return hand.getValue() < 15;
    }

    @Override
    public String getName() {
        return "Conservadora";
    }
}