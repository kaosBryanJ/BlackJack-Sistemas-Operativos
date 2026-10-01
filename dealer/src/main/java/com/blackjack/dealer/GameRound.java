package com.blackjack.dealer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class GameRound {

    private final int playerCount;
    private final int roundNumber;

    private final List<PlayerConnectionHandler> handlers;
    private final List<Player> players;

    private final Semaphore playersReady;
    private final Semaphore startSignal;
    private final Semaphore playersFinished;
    private final Semaphore resultsReady;

    private final Semaphore[] playerTurns;

    private Game game;

    public GameRound(int playerCount, int roundNumber) {

        this.playerCount = playerCount;
        this.roundNumber = roundNumber;

        handlers = new ArrayList<>();
        players = new ArrayList<>();

        playersReady = new Semaphore(0);
        startSignal = new Semaphore(0);
        playersFinished = new Semaphore(0);
        resultsReady = new Semaphore(0);

        playerTurns = new Semaphore[playerCount];

        for (int i = 0; i < playerCount; i++) {
            playerTurns[i] = new Semaphore(0);
        }

        // El Jugador 1 comienza.
        playerTurns[0].release();
    }

    public synchronized boolean registerPlayer(
        PlayerConnectionHandler handler,
        Player player
    ) {

        if (players.size() >= playerCount) {
            return false;
        }

        handlers.add(handler);
        players.add(player);

        System.out.println(
            "Jugador registrado: "
            + player.getName()
            + " ["
            + players.size()
            + "/"
            + playerCount
            + "]"
        );

        playersReady.release();

        return true;
    }

    public void awaitAllPlayersReady()
        throws InterruptedException {

        for (int i = 0; i < playerCount; i++) {
            playersReady.acquire();
        }
    }

    public void startPlayers() {
        startSignal.release(playerCount);
    }

    public void awaitGameStart()
        throws InterruptedException {

        startSignal.acquire();
    }

    public void awaitTurn(int index)
        throws InterruptedException {

        playerTurns[index].acquire();
    }

    public void finishPlayerTurn(int index) {

        if (index + 1 < playerCount) {
            playerTurns[index + 1].release();
        }

        playersFinished.release();
    }

    public void awaitAllPlayerTurns()
        throws InterruptedException {

        for (int i = 0; i < playerCount; i++) {
            playersFinished.acquire();
        }
    }

    public void publishResults() {
        resultsReady.release(playerCount);
    }

    public void awaitResults()
        throws InterruptedException {

        resultsReady.acquire();
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public synchronized List<PlayerConnectionHandler> getHandlers() {
        return new ArrayList<>(handlers);
    }

    public synchronized List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public synchronized void setGame(Game game) {
        this.game = game;
    }

    public synchronized Game getGame() {
        return game;
    }
}