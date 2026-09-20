package com.blackjack.dealer;

public class AggressiveStrategy implements PlayerStrategy {

    @Override
    public boolean shouldHit(Hand hand) {
        return hand.getValue() < 19;
    }

    @Override
    public String getName() {
        return "Agresiva";
    }
}
