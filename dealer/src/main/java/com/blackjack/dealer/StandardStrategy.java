package com.blackjack.dealer;

public class StandardStrategy implements PlayerStrategy {

    @Override
    public boolean shouldHit(Hand hand) {
        return hand.getValue() < 17;
    }

    @Override
    public String getName() {
        return "Normal";
    }
}
