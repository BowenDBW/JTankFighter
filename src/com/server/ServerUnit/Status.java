package com.server.ServerUnit;

/**
 * <p>
 *
 * @author BowenDeng
 * @version 1.0
 * @date 2021/12/19 14:11
 **/
public class Status {

    private static boolean serverCreated;
    private static boolean gameStarted;
    private static boolean gamePaused;
    private static boolean gameOver;
    private static boolean serverVoteYes, serverVoteNo;
    private static boolean clientVoteYes;
    private static boolean pausePressed;
    public static boolean isServerCreated() {
        return serverCreated;
    }

    public static boolean isGameStarted() {
        return gameStarted;
    }


    public static boolean isGamePaused() {
        return gamePaused;
    }

    public static void setGamePaused(boolean gamePaused) {
        Status.gamePaused = gamePaused;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver) {
        Status.gameOver = gameOver;
    }

    public static boolean isServerVoteYes() {
        return serverVoteYes;
    }

    public static void setServerVoteYes(boolean serverVoteYes) {
        Status.serverVoteYes = serverVoteYes;
    }

    public static void setServerVoteNo(boolean serverVoteNo) {
        Status.serverVoteNo = serverVoteNo;
    }

    public static void setClientVoteYes(boolean clientVoteYes) {
        Status.clientVoteYes = clientVoteYes;
    }

    public static void setPausePressed(boolean pausePressed) {
        Status.pausePressed = pausePressed;
    }


    public static void setServerCreated(boolean serverCreated) {
        Status.serverCreated = serverCreated;
    }

    public static void setGameStarted(boolean gameStarted) {
        Status.gameStarted = gameStarted;
    }

    public static boolean isServerVoteNo() {
        return serverVoteNo;
    }

    public static boolean isClientVoteYes() {
        return clientVoteYes;
    }

    public static boolean isPausePressed() {
        return pausePressed;
    }
}
