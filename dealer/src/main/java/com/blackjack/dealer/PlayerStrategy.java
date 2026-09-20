package com.blackjack.dealer;

public interface PlayerStrategy {

    boolean shouldHit(Hand hand);

    String getName();
}