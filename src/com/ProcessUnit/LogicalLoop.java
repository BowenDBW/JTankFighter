package com.ProcessUnit;


import com.ProcessUnit.Instruction;
import com.client.ClientUnit.*;
import com.server.ComponentPack.Enemy;
import com.server.ComponentPack.GameComponent;
import com.server.ServerUnit.FeedbackHandler;
import com.server.ServerUnit.ServerCommunication;
import com.server.ServerUnit.ServerModel;

/**
 * @author chenhong
 * @version 1.0
 * @description TODO
 * @date 2021/12/17 21:48
 */
public class LogicalLoop {

    public static void clientLogic() {
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
                Instruction.setFromUser(new StringBuffer());

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

    public static void serverLogic() {

        try {

            String line;
            while ((line = ServerCommunication.getIn().readLine()) != null) {
                //处理客户反馈消息
                FeedbackHandler.handleInstruction(line);

                if (!com.server.ServerUnit.Status.isGamePaused()) {

                    ServerModel.setGameFlow(ServerModel.getGameFlow() + 1);
                }

                if (!com.server.ServerUnit.Status.isPausePressed()) {

                    if (!com.server.ServerUnit.Status.isGamePaused()) {

                        Instruction.getFromSever().append("x0;");
                    } else {

                        Instruction.getFromSever().append("x1;");
                    }
                    com.server.ServerUnit.Status.setPausePressed(false);
                }

                if (com.server.ServerUnit.Status.isGameOver() ||
                        (ServerModel.getP1().getLife() == 0 && ServerModel.getP2().getLife() == 0)) {

                    if (ServerModel.getP1().getFrozen() != 1) {

                        Instruction.getFromSever().append("a;");
                    }

                    boolean isRespond =
                            (ServerModel.getP1().getFrozen() != 1 || com.server.ServerUnit.DrawingPanel.getMessageIndex() == 1) && com.server.ServerUnit.Status.isServerVoteYes();

                    if (isRespond) {

                        com.server.ServerUnit.DrawingPanel.addMessage("等待用户端玩家的回应...");
                    }
                    if (ServerModel.getP1().getFrozen() != 1 || com.server.ServerUnit.DrawingPanel.getMessageIndex() == 0) {

                        com.server.ServerUnit.DrawingPanel.addMessage("GAME OVER ! 　想再玩一次吗 ( y / n ) ?");
                    }
                    com.server.ServerUnit.Status.setGameOver(true);
                    ServerModel.getP1().setFrozen(1);
                    ServerModel.getP2().setFrozen(1);

                    if (com.server.ServerUnit.Status.isServerVoteNo() && !com.server.ServerUnit.Status.isServerVoteYes()){

                        System.exit(0);
                    }

                    if (com.server.ServerUnit.Status.isServerVoteYes()) {

                        Instruction.getFromSever().append("j;");
                        if (com.server.ServerUnit.Status.isClientVoteYes()) {

                            ServerModel.restartGame();
                        }
                    }
                }

                if (com.server.ServerUnit.Level.getDeathCount() == 20 && !com.server.ServerUnit.Status.isGameOver()) {
                    int winningCount = com.server.ServerUnit.Level.getWinningCount();
                    winningCount++;
                    com.server.ServerUnit.Level.setWinningCount(winningCount);
                    if (com.server.ServerUnit.Level.getWinningCount() == 120) {
                        ServerModel.getP1().setFrozen(1);
                        ServerModel.getP2().setFrozen(1);
                    }
                    if (com.server.ServerUnit.Level.getWinningCount() == 470) {
                        if (ServerModel.getP1().getLife() > 0) {
                            ServerModel.getP1().reset();
                        }
                        if (ServerModel.getP2().getLife() > 0) {
                            ServerModel.getP2().reset();
                        }
                        com.server.ServerUnit.Level.loadLevel();
                        //告诉客户端程序加载下一关
                        Instruction.getFromSever().append("L")
                                .append(1 + (com.server.ServerUnit.Level.getCurrentLevel() - 1) % 8).append(";");
                    }
                    if (com.server.ServerUnit.Level.getWinningCount() == 500) {
                        ServerModel.getP1().setFrozen(0);
                        ServerModel.getP2().setFrozen(0);
                        com.server.ServerUnit.Level.setDeathCount(0);
                        com.server.ServerUnit.Level.setWinningCount(0);
                    }

                }

                //大量生产敌人坦克
                if (!com.server.ServerUnit.Status.isGamePaused()) {
                    com.server.ServerUnit.Level.spawnEnemy();
                }

                for (GameComponent gameComponent : com.server.ServerUnit.DrawingPanel.gameComponents) {
                    if (gameComponent != null) {
                        gameComponent.move();
                    }
                }

                //从消息队列中删除一个消息每10秒，（如果有的话）
                ServerModel.setGameFlow(ServerModel.getGameFlow() % 300);
                if (ServerModel.getGameFlow() == 0) {
                    com.server.ServerUnit.DrawingPanel.removeMessage();
                }

                //将玩家、关卡的信息写入输出行
                Instruction.getFromSever().append("p").append(com.server.ServerUnit.Level.getEnemyLeft())
                        .append(",").append(com.server.ServerUnit.Level.getCurrentLevel())
                        .append(",").append(ServerModel.getP1().getLife()).append(",").append(ServerModel.getP1().scores)
                        .append(",").append(ServerModel.getP2().getLife()).append(",").append(ServerModel.getP2().scores)
                        .append(";");
                Instruction.getFromSever().append("g").append(com.server.ServerUnit.Level.getWinningCount())
                        .append(";");

                //将玩家类型信息写入输出行
                if (!"".equals(ServerModel.getPlayerTypedMessage())) {
                    Instruction.getFromSever().append(ServerModel.getPlayerTypedMessage());
                    ServerModel.setPlayerTypedMessage("");
                }

                //将最后的指令字符串发送到客户端程序
                ServerCommunication.getOut().println(Instruction.getFromSever());
                Instruction.setFromSever(new StringBuffer());
                //调用视图重绘本身
                ServerModel.getView().getMainPanel().repaint();

                //如果玩家切换到对话框模式，则停止所有坦克动作
                if (!ServerModel.getView().getMainPanel().hasFocus()) {
                    ServerModel.getP1().setMoveLeft(false);
                    ServerModel.getP1().setMoveUp(false);
                    ServerModel.getP1().setMoveDown(false);
                    ServerModel.getP1().setMoveRight(false);
                    ServerModel.getP1().setFire(false);
                }

                Thread.sleep(30);
            }
        } catch (Exception ex) {

            ex.printStackTrace();
            ServerModel.getView().getMessageField().setEnabled(false);
            com.server.ServerUnit.Status.setServerVoteYes(false);
            com.server.ServerUnit.Status.setServerVoteNo (false);
            com.server.ServerUnit.Status.setClientVoteYes(false);
            com.server.ServerUnit.Status.setServerCreated(false);
            com.server.ServerUnit.Status.setGameStarted(false);
            com.server.ServerUnit.Status.setGameOver(false);
            ServerModel.setGameFlow(0);
            Enemy.setFrozenTime(0);
            Enemy.setFrozenMoment(0);
            ServerModel.getView().getMainPanel().setGameStarted(false);
            ServerModel.getT().stop();
            com.server.ServerUnit.DrawingPanel.addMessage("玩家退出了，请重新建立主机");

            //当发生错误在游戏中，摧毁任何东西，包括游戏的变量
            try {

                ServerCommunication.getOut().close();
                ServerCommunication.getIn().close();
                ServerCommunication.getClientSocket().close();
                ServerCommunication.getServerSocket().close();
            } catch (Exception exc) {

                exc.printStackTrace();
            }

            //破坏游戏数据
            ServerModel.setP1(null);
            ServerModel.setP2(null);
            com.server.ServerUnit.Level.reset();
        }
    }
}
