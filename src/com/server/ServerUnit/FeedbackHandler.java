package com.server.ServerUnit;
//这个类从客户端程序解码指令字符串,然后将字符串转换为真正的指令
//服务器程序可读
import com.ProcessUnit.Instruction;

/**
 * @author chenhong
 */
public class FeedbackHandler {

    public static void handleInstruction(String instruction) {

        if (instruction == null) {

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

                ServerModel.getP2().setMoveUp(false);
                ServerModel.getP2().setMoveDown(false);
                ServerModel.getP2().setMoveLeft(false);
                ServerModel.getP2().setMoveRight(false);
                ServerModel.getP2().setFire(false);

                String temp = perInstruction.substring(1, 2);
                if ("1".equals(temp)) {

                    ServerModel.getP2().setMoveUp(true);
                }
                temp = perInstruction.substring(2, 3);
                if ("1".equals(temp)) {

                    ServerModel.getP2().setMoveDown(true);
                }
                temp = perInstruction.substring(3, 4);
                if ("1".equals(temp)) {

                    ServerModel.getP2().setMoveLeft(true);
                }
                temp = perInstruction.substring(4, 5);
                if ("1".equals(temp)) {

                    ServerModel.getP2().setMoveRight(true);
                }
                temp = perInstruction.substring(5, 6);
                if ("1".equals(temp)) {

                    ServerModel.getP2().setFire(true);
                }
            }

            //指令是“m”表示服务器玩家信息
            if ("e".equals(perInstruction.substring(0, 1))) {

                DrawingPanel.addMessage("用户端玩家说：" + perInstruction.substring(1, perInstruction.length()));
            }

            //指令是“j”表示客户端玩家想在玩一次
            if ("j".equals(perInstruction.substring(0, 1))) {

                if (Status.isGameOver()) {

                    Status.setClientVoteYes(true);
                }
            }

            //指令是“x”表示服务器玩家暂停游戏
            if ("x".equals(perInstruction.substring(0, 1))) {

                if (Status.isGamePaused()) {

                    DrawingPanel.addMessage("用户端玩家取消了暂停");
                    Status.setGamePaused(false);
                } else {

                    DrawingPanel.addMessage("用户端玩家暂停了游戏");
                    Status.setGamePaused(true);
                }
            }
            i++;
        }
    }

}