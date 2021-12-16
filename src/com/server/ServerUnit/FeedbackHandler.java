package com.server.ServerUnit;
//这个类从客户端程序解码指令字符串,然后将字符串转换为真正的指令
//服务器程序可读

/**
 * @author chenhong
 */
public class FeedbackHandler {

    public static void handleInstruction(ServerModel gameModel, String instruction) {
        if (instruction.length() == 0) {
            return;
        }

        int i = 0;
        while (i < instruction.length()) {
            StringBuilder perInstruction = new StringBuilder();

            //指令是“；”时
            while (instruction.charAt(i) != ';') {
                perInstruction.append(instruction.charAt(i));
                i++;
            }

            //指令是“m”表明客户端运动信息
            if ("m".equals(perInstruction.substring(0, 1))) {
                gameModel.getP2().setMoveUp(false);
                gameModel.getP2().setMoveDown(false);
                gameModel.getP2().setMoveLeft(false);
                gameModel.getP2().setMoveRight(false);
                gameModel.getP2().setFire(false);

                String temp = perInstruction.substring(1, 2);
                if ("1".equals(temp)) {
                    gameModel.getP2().setMoveUp(true);
                }
                temp = perInstruction.substring(2, 3);
                if ("1".equals(temp)) {
                    gameModel.getP2().setMoveDown(true);
                }
                temp = perInstruction.substring(3, 4);
                if ("1".equals(temp)) {
                    gameModel.getP2().setMoveLeft(true);
                }
                temp = perInstruction.substring(4, 5);
                if ("1".equals(temp)) {
                    gameModel.getP2().setMoveRight(true);
                }
                temp = perInstruction.substring(5, 6);
                if ("1".equals(temp)) {
                    gameModel.getP2().setFire(true);
                }
            }

            //指令是“m”表示服务器玩家信息
            if ("e".equals(perInstruction.substring(0, 1))) {
                gameModel.addMessage("用户端玩家说：" + perInstruction.substring(1, perInstruction.length()));
            }

            //指令是“j”表示客户端玩家想在玩一次
            if ("j".equals(perInstruction.substring(0, 1))) {
                if (gameModel.isGameOver()) {
                    gameModel.setClientVoteYes(true);
                }
            }

            //指令是“x”表示服务器玩家暂停游戏
            if ("x".equals(perInstruction.substring(0, 1))) {
                if (gameModel.isGamePaused()) {
                    gameModel.addMessage("用户端玩家取消了暂停");
                    gameModel.setGamePaused(false);
                } else {
                    gameModel.addMessage("用户端玩家暂停了游戏");
                    gameModel.setGamePaused(true);
                }
            }
            i++;
        }
    }

}