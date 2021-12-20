package com.ProcessUnit.ServerPack;

/**
 * <p>
 *
 * @author BowenDeng
 * @version 1.0
 * @date 2021/12/19 14:11
 **/
public class ServerStatus {

    private static boolean serverCreated;
    private static boolean gameStarted;
    private static boolean gamePaused;
    private static boolean gameOver;
    private static boolean serverVoteYes, serverVoteNo;
    private static boolean clientVoteYes;
    private static boolean pausePressed;

    @SuppressWarnings("all")
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
        ServerStatus.gamePaused = gamePaused;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver) {
        ServerStatus.gameOver = gameOver;
    }

    public static boolean isServerVoteYes() {
        return serverVoteYes;
    }

    public static void setServerVoteYes(boolean serverVoteYes) {
        ServerStatus.serverVoteYes = serverVoteYes;
    }

    public static void setServerVoteNo(boolean serverVoteNo) {
        ServerStatus.serverVoteNo = serverVoteNo;
    }

    public static void setClientVoteYes(boolean clientVoteYes) {
        ServerStatus.clientVoteYes = clientVoteYes;
    }

    public static void setPausePressed(boolean pausePressed) {
        ServerStatus.pausePressed = pausePressed;
    }


    public static void setServerCreated(boolean serverCreated) {
        ServerStatus.serverCreated = serverCreated;
    }

    public static void setGameStarted(boolean gameStarted) {
        ServerStatus.gameStarted = gameStarted;
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
