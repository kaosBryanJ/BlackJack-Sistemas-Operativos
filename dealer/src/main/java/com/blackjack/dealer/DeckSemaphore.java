package com.blackjack.dealer;

import java.util.concurrent.Semaphore;

public class DeckSemaphore {

    private final Semaphore semaphore;

    public DeckSemaphore() {
        semaphore = new Semaphore(1);
    }

    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }
}