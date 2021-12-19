package com.client.ClientUnit;

/**
 * @author chenhong
 * @version 1.0
 * @description 这个类主要用来存储各状态变量
 * @date 2021/12/17 21:20
 */
public class Status {

    private static boolean serverConnected;
    private static boolean gameStarted;
    private static boolean gamePaused;
    private static boolean gameOver;
    private static boolean serverVote;
    private static boolean clientVoteYes, clientVoteNo;
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
        Status.serverConnected = serverConnected;
    }

    public static boolean isGameStarted() {
        return gameStarted;
    }

    public static void setGameStarted(boolean gameStarted) {
        Status.gameStarted = gameStarted;
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

    public static boolean isServerVote() {
        return serverVote;
    }

    public static void setServerVote(boolean serverVote) {
        Status.serverVote = serverVote;
    }

    public static boolean isClientVoteYes() {
        return clientVoteYes;
    }

    public static void setClientVoteYes(boolean clientVoteYes) {
        Status.clientVoteYes = clientVoteYes;
    }

    public static boolean isClientVoteNo() {
        return clientVoteNo;
    }

    public static void setClientVoteNo(boolean clientVoteNo) {
        Status.clientVoteNo = clientVoteNo;
    }

    public static boolean isPausePressed() {
        return pausePressed;
    }

    public static void setPausePressed(boolean pausePressed) {
        Status.pausePressed = pausePressed;
    }

    public static boolean isMoveUp() {
        return moveUp;
    }

    public static void setMoveUp(boolean moveUp) {
        Status.moveUp = moveUp;
    }

    public static boolean isMoveDown() {
        return moveDown;
    }

    public static void setMoveDown(boolean moveDown) {
        Status.moveDown = moveDown;
    }

    public static boolean isMoveLeft() {
        return moveLeft;
    }

    public static void setMoveLeft(boolean moveLeft) {
        Status.moveLeft = moveLeft;
    }

    public static boolean isMoveRight() {
        return moveRight;
    }

    public static void setMoveRight(boolean moveRight) {
        Status.moveRight = moveRight;
    }

    public static boolean isFire() {
        return fire;
    }

    public static void setFire(boolean fire) {
        Status.fire = fire;
    }
}
