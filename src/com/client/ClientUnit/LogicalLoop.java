package com.client.ClientUnit;

/**
 * @author chenhong
 * @version 1.0
 * @description TODO
 * @date 2021/12/17 21:48
 */
public class LogicalLoop {
    private static ClientModel clientModel = new ClientModel(ClientModel.getView());

    private static int gameFlow;

    public static void logic() {
        //游戏逻辑循环,客户端程序实际不执行任何逻辑计算,它只接受drawing-instructions
        try {
            String fromServer;
            while ((fromServer = clientModel.getClientCommunication().getIn().readLine()) != null) {

                gameFlow = clientModel.getGameFlow();
                gameFlow++;
                clientModel.setGameFlow(gameFlow);

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
                            clientModel.addMessage("主机端玩家决定再玩一次，游戏重新开始了...");
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
                InstructionHandler.handleInstruction(clientModel, fromServer);

                //从消息队列中删除一个消息每10秒,(如果有)
                if (gameFlow % 300 == 0) {
                    clientModel.removeMessage();
                }

                //输出玩家坦克信息
                if (!"".equals(clientModel.getPlayerTypedMessage())) {
                    Instruction.getFromUser().append(clientModel.getPlayerTypedMessage());
                    clientModel.setPlayerTypedMessage("");
                }

                //发送反馈指令
                clientModel.getClientCommunication().getOut().println(Instruction.getFromUser().toString());

                //调用视图重新绘制它自己
                clientModel.getView().getMainPanel().repaint();

                //如果切换到对话模式的玩家,那么停止所有坦克行动
                if (!clientModel.getView().getMainPanel().hasFocus()) {
                    Status.setMoveLeft(false);
                    Status.setMoveUp(false);
                    Status.setMoveDown(false);
                    Status.setMoveRight(false);
                    Status.setFire(false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            clientModel.getT().stop();
            clientModel.getView().getMessageField().setEnabled(false);
            Status.setServerConnected(false);
            Status.setGameStarted(false);
            clientModel.getView().getMainPanel().setGameStarted(false);
            Status.setGameOver(false);
            clientModel.addMessage("主机端退出了");
            clientModel.getView().getIPField().setFocusable(true);
            clientModel.getView().getIPField().setEnabled(true);

            //当有错误发生时,关闭创建的任何事情
            try {
                clientModel.getClientCommunication().getOut().close();
                clientModel.getClientCommunication().getIn().close();
                clientModel.getClientCommunication().getClientSocket().close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
}