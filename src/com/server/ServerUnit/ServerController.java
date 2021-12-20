package com.server.ServerUnit;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//这个类处理来自服务器视图的输入
public class ServerController {
    private final ServerView view;
    private final ServerModel model;
    private int helpMessageCount = 1;
    //一个玩家坦克的参考

    public ServerController(ServerView thisView, ServerModel thisModel) {
        view = thisView;
        model = thisModel;

        //操作发送消息按钮的动作
        view.getSendMessage().addActionListener(e -> {
            if (!Status.isGameStarted()) {
                DrawingPanel.addMessage("还没有和别的玩家联上, 无法发送对话");
                return;
            }

            if (!"".equals(view.getMessageField().getText())) {
                DrawingPanel.addMessage("主机端玩家说：" + view.getMessageField().getText());
                model.playerTypedMessage += "m" + view.getMessageField().getText() + ";";
                view.getMessageField().setText("");
            } else {
                DrawingPanel.addMessage("对话内容不能为空");
            }
        }
        );

        //操作建立主机按钮的动作
        view.getCreateServer().addActionListener(e -> {
            if (!Status.isServerCreated()) {
                ServerModel.getT().start();
            }
        }
        );

        //操作暂停/继续按钮的动作
        view.getPauseAndResume().addActionListener(e -> {
            Status.setPausePressed(true);
            if (!Status.isGameOver() && Status.isGameStarted()) {
                if (!Status.isGamePaused()) {
                    Status.setGamePaused(true);
                    DrawingPanel.addMessage("主机端玩家暂停了游戏");
                } else {
                    Status.setGamePaused(false);
                    DrawingPanel.addMessage("主机端玩家取消了暂停");
                }
            }
        }
        );

        //操作帮助按钮的动作
        view.getHelp().addActionListener(e -> {
                    DrawingPanel.addMessage("-------------------------------坦克大战 1.0-----------------------------------");
                    DrawingPanel.addMessage("帮助: 按 s 键开火,  按键盘的方向键来控制坦克的移动");
                    DrawingPanel.addMessage("如果按键没有反应请 1. 关闭大写功能; 2. 用 tab键切换 ");
                    DrawingPanel.addMessage("到控制界面如果您在使用对话界面.");
                    DrawingPanel.addMessage("--------------------------------------------------------------------------------------");
        }
        );

        //操作退出按钮的动作
        view.getExit().addActionListener(e -> System.exit(0)
        );

        //操作输入按钮的动作
        view.getMessageField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (helpMessageCount > 0) {
                    DrawingPanel.addMessage("提示：用\"tab\"键可以自由切换于控制界面和对话界面");
                    DrawingPanel.addMessage("提示：按回车键可以直接发送您的对话");
                    helpMessageCount--;
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!"".equals(view.getMessageField().getText())) {
                        DrawingPanel.addMessage("主机端玩家说：" + view.getMessageField().getText());
                        model.playerTypedMessage += "m" + view.getMessageField().getText() + ";";
                        view.getMessageField().setText("");
                    } else {
                        DrawingPanel.addMessage("对话内容不能为空");
                    }
                }
            }
        });

        JPanel temp = view.getMainPanel();
        temp.addKeyListener(new KeyAdapter() {
                                @Override
                                public void keyPressed(KeyEvent e) {
                                    if (ServerModel.getP1() != null) {
                                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                                            ServerModel.getP1().setMoveUp(true);
                                            ServerModel.getP1().setMoveDown(false);
                                            ServerModel.getP1().setMoveLeft(false);
                                            ServerModel.getP1().setMoveRight(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                            ServerModel.getP1().setMoveDown(true);
                                            ServerModel.getP1().setMoveUp(false);
                                            ServerModel.getP1().setMoveLeft(false);
                                            ServerModel.getP1().setMoveRight(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                                            ServerModel.getP1().setMoveLeft(true);
                                            ServerModel.getP1().setMoveUp(false);
                                            ServerModel.getP1().setMoveDown(false);
                                            ServerModel.getP1().setMoveRight(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                                            ServerModel.getP1().setMoveLeft(false);
                                            ServerModel.getP1().setMoveUp(false);
                                            ServerModel.getP1().setMoveDown(false);
                                            ServerModel.getP1().setMoveRight(true);
                                        }
                                        if (e.getKeyChar() == 's') {
                                            ServerModel.getP1().setFire(true);
                                        }

                                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                            if (!"".equals(view.getMessageField().getText())) {
                                                DrawingPanel.addMessage("主机端玩家说：" + view.getMessageField().getText());
                                                model.playerTypedMessage += "m" + view.getMessageField().getText() + ";";
                                                view.getMessageField().setText("");
                                            }
                                        }

                                        if (e.getKeyChar() == 'y' && Status.isGameOver() && !Status.isServerVoteYes()) {
                                            Status.setServerVoteYes(true);
                                            DrawingPanel.addMessage("等待用户端玩家的回应...");
                                        }

                                        if (e.getKeyChar() == 'n' && Status.isGameOver()) {
                                            Status.setServerVoteNo(true);
                                        }
                                    }
                                }

                                @Override
                                public void keyReleased(KeyEvent e) {
                                    if (ServerModel.getP1() != null) {
                                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                                            ServerModel.getP1().setMoveUp(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                            ServerModel.getP1().setMoveDown(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                                            ServerModel.getP1().setMoveLeft(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                                            ServerModel.getP1().setMoveRight(false);
                                        }
                                        if (e.getKeyChar() == 's') {
                                            ServerModel.getP1().setFire(false);
                                        }
                                    }
                                }
                            }
        );

    }
}