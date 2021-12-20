package com.ProcessUnit.ClientPack;

/**
 * @author chenhong
 * @version 1.0
 * @description 这个类主要用来存储各状态变量
 * @date 2021/12/17 21:20
 */
public class ClientStatus {

    private static boolean serverConnected;
    private static boolean gameStarted;
    private static boolean gamePaused;
    private static boolean gameOver;
    private static boolean serverVote;
    private static boolean clientVoteYes;
    private static boolean clientVoteNo;
    private static boolean pausePressed;

    private static boolean moveUp;
    private static boolean moveDown;
    private static boolean moveLeft;
    private static boolean moveRight;
    private static boolean fire;

    public static boolean isServerConnected() {
        return !serverConnected;
    }

    public static void setServerConnected(boolean serverConnected) {
        ClientStatus.serverConnected = serverConnected;
    }

    public static boolean isGameStarted() {
        return gameStarted;
    }

    public static void setGameStarted(boolean gameStarted) {
        ClientStatus.gameStarted = gameStarted;
    }

    public static boolean isGamePaused() {
        return gamePaused;
    }

    public static void setGamePaused(boolean gamePaused) {
        ClientStatus.gamePaused = gamePaused;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver) {
        ClientStatus.gameOver = gameOver;
    }

    public static boolean isServerVote() {
        return serverVote;
    }

    public static void setServerVote(boolean serverVote) {
        ClientStatus.serverVote = serverVote;
    }

    public static boolean isClientVoteYes() {
        return clientVoteYes;
    }

    public static void setClientVoteYes(boolean clientVoteYes) {
        ClientStatus.clientVoteYes = clientVoteYes;
    }

    public static boolean isClientVoteNo() {
        return clientVoteNo;
    }

    public static void setClientVoteNo(boolean clientVoteNo) {
        ClientStatus.clientVoteNo = clientVoteNo;
    }

    public static boolean isPausePressed() {
        return pausePressed;
    }

    public static void setPausePressed(boolean pausePressed) {
        ClientStatus.pausePressed = pausePressed;
    }

    public static boolean isMoveUp() {
        return moveUp;
    }

    public static void setMoveUp(boolean moveUp) {
        ClientStatus.moveUp = moveUp;
    }

    public static boolean isMoveDown() {
        return moveDown;
    }

    public static void setMoveDown(boolean moveDown) {
        ClientStatus.moveDown = moveDown;
    }

    public static boolean isMoveLeft() {
        return moveLeft;
    }

    public static void setMoveLeft(boolean moveLeft) {
        ClientStatus.moveLeft = moveLeft;
    }

    public static boolean isMoveRight() {
        return moveRight;
    }

    public static void setMoveRight(boolean moveRight) {
        ClientStatus.moveRight = moveRight;
    }

    public static boolean isFire() {
        return fire;
    }

    public static void setFire(boolean fire) {
        ClientStatus.fire = fire;
    }
}
