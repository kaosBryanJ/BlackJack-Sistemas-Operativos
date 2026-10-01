package com.blackjack.dealer;

import java.util.concurrent.Semaphore;

public class DeckSemaphore {

    private final Semaphore semaphore;

    public DeckSemaphore() {
        // Un solo permiso: solo un hilo puede acceder
        // a la baraja al mismo tiempo.
        semaphore = new Semaphore(1, true);
    }

    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }
}