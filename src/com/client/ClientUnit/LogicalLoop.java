package com.client.ClientUnit;


/**
 * @author chenhong
 * @version 1.0
 * @description TODO
 * @date 2021/12/17 21:48
 */
public class LogicalLoop {

    public static void logic() {
        //游戏逻辑循环,客户端程序实际不执行任何逻辑计算,它只接受drawing-instructions
        try {
            String fromServer;
            while ((fromServer = ClientCommunication.getIn().readLine()) != null) {

                int gameFlow = ClientModel.getGameFlow();
                gameFlow++;
                ClientModel.setGameFlow(gameFlow);

                if (Status.isPausePressed()) {
                    Instruction.getFromUser().append("x;");
                    Status.setPausePressed(false);
                }

                if (Status.isGameOver()) {
                    if (Status.isClientVoteNo()) {
                        System.exit(0);
                    }

                    if (Status.isClientVoteYes()) {
                        Instruction.getFromUser().append("j;");
                        if (Status.isServerVote()) {
                            DrawingPanel.addMessage("主机端玩家决定再玩一次，游戏重新开始了...");
                            Status.setGameOver(false);
                            Status.setClientVoteYes(false);
                            Status.setServerVote(false);
                        }
                    }
                }

                //指令字符串做出反馈,告诉服务器客户端在做什么
                Instruction.getFromUser().append("m");
                if (Status.isMoveUp()) {
                    Instruction.getFromUser().append("1");
                } else {
                    Instruction.getFromUser().append("0");
                }
                if (Status.isMoveDown()) {
                    Instruction.getFromUser().append("1");
                } else {
                    Instruction.getFromUser().append("0");
                }
                if (Status.isMoveLeft()) {
                    Instruction.getFromUser().append("1");
                } else {
                    Instruction.getFromUser().append("0");
                }
                if (Status.isMoveRight()) {
                    Instruction.getFromUser().append("1");
                } else {
                    Instruction.getFromUser().append("0");
                }
                if (Status.isFire()) {
                    Instruction.getFromUser().append("1");
                } else {
                    Instruction.getFromUser().append("0");
                }
                Instruction.getFromUser().append(";");

                //来自服务器的进程指令
                InstructionHandler.handleInstruction(fromServer);

                //从消息队列中删除一个消息每10秒,(如果有)
                if (gameFlow % 300 == 0) {
                    DrawingPanel.removeMessage();
                }

                //输出玩家坦克信息
                if (!"".equals(ClientModel.getPlayerTypedMessage())) {
                    Instruction.getFromUser().append(ClientModel.getPlayerTypedMessage());
                    ClientModel.setPlayerTypedMessage("");
                }

                //发送反馈指令
                ClientCommunication.getOut().println(Instruction.getFromUser().toString());

                //调用视图重新绘制它自己
                ClientModel.getView().getMainPanel().repaint();

                //如果切换到对话模式的玩家,那么停止所有坦克行动
                if (!ClientModel.getView().getMainPanel().hasFocus()) {
                    Status.setMoveLeft(false);
                    Status.setMoveUp(false);
                    Status.setMoveDown(false);
                    Status.setMoveRight(false);
                    Status.setFire(false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ClientModel.getT().stop();
            ClientModel.getView().getMessageField().setEnabled(false);
            Status.setServerConnected(false);
            Status.setGameStarted(false);

            Status.setGameOver(false);
            DrawingPanel.addMessage("主机端退出了");
            ClientModel.getView().getIpField().setFocusable(true);
            ClientModel.getView().getIpField().setEnabled(true);

            //当有错误发生时,关闭创建的任何事情
            try {
                ClientCommunication.getOut().close();
                ClientCommunication.getIn().close();
                ClientCommunication.getClientSocket().close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
}
