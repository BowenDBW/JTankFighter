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
                model.addMessage("还没有和别的玩家联上, 无法发送对话");
                return;
            }

            if (!"".equals(view.getMessageField().getText())) {
                model.addMessage("主机端玩家说：" + view.getMessageField().getText());
                model.playerTypedMessage += "m" + view.getMessageField().getText() + ";";
                view.getMessageField().setText("");
            } else {
                model.addMessage("对话内容不能为空");
            }
        }
        );

        //操作建立主机按钮的动作
        view.getCreateServer().addActionListener(e -> {
            if (!Status.isServerCreated()) {
                model.getT().start();
            }
        }
        );

        //操作暂停/继续按钮的动作
        view.getPauseAndResume().addActionListener(e -> {
            Status.setPausePressed(true);
            if (!Status.isGameOver() && Status.isGameStarted()) {
                if (!Status.isGamePaused()) {
                    Status.setGamePaused(true);
                    model.addMessage("主机端玩家暂停了游戏");
                } else {
                    Status.setGamePaused(false);
                    model.addMessage("主机端玩家取消了暂停");
                }
            }
        }
        );

        //操作帮助按钮的动作
        view.getHelp().addActionListener(e -> {
            model.addMessage("-------------------------------坦克大战 1.0-----------------------------------");
            model.addMessage("帮助: 按 s 键开火,  按键盘的方向键来控制坦克的移动");
            model.addMessage("如果按键没有反应请 1. 关闭大写功能; 2. 用 tab键切换 ");
            model.addMessage("到控制界面如果您在使用对话界面.");
            model.addMessage("--------------------------------------------------------------------------------------");
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
                    model.addMessage("提示：用\"tab\"键可以自由切换于控制界面和对话界面");
                    model.addMessage("提示：按回车键可以直接发送您的对话");
                    helpMessageCount--;
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!"".equals(view.getMessageField().getText())) {
                        model.addMessage("主机端玩家说：" + view.getMessageField().getText());
                        model.playerTypedMessage += "m" + view.getMessageField().getText() + ";";
                        view.getMessageField().setText("");
                    } else {
                        model.addMessage("对话内容不能为空");
                    }
                }
            }
        });

        JPanel temp = view.getMainPanel();
        temp.addKeyListener(new KeyAdapter() {
                                @Override
                                public void keyPressed(KeyEvent e) {
                                    if (model.getP1() != null) {
                                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                                            model.getP1().setMoveUp(true);
                                            model.getP1().setMoveDown(false);
                                            model.getP1().setMoveLeft(false);
                                            model.getP1().setMoveRight(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                            model.getP1().setMoveDown(true);
                                            model.getP1().setMoveUp(false);
                                            model.getP1().setMoveLeft(false);
                                            model.getP1().setMoveRight(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                                            model.getP1().setMoveLeft(true);
                                            model.getP1().setMoveUp(false);
                                            model.getP1().setMoveDown(false);
                                            model.getP1().setMoveRight(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                                            model.getP1().setMoveLeft(false);
                                            model.getP1().setMoveUp(false);
                                            model.getP1().setMoveDown(false);
                                            model.getP1().setMoveRight(true);
                                        }
                                        if (e.getKeyChar() == 's') {
                                            model.getP1().setFire(true);
                                        }

                                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                            if (!"".equals(view.getMessageField().getText())) {
                                                model.addMessage("主机端玩家说：" + view.getMessageField().getText());
                                                model.playerTypedMessage += "m" + view.getMessageField().getText() + ";";
                                                view.getMessageField().setText("");
                                            }
                                        }

                                        if (e.getKeyChar() == 'y' && Status.isGameOver() && !Status.isServerVoteYes()) {
                                            Status.setServerVoteYes(true);
                                            model.addMessage("等待用户端玩家的回应...");
                                        }

                                        if (e.getKeyChar() == 'n' && Status.isGameOver()) {
                                            Status.setServerVoteNo(true);
                                        }
                                    }
                                }

                                @Override
                                public void keyReleased(KeyEvent e) {
                                    if (model.getP1() != null) {
                                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                                            model.getP1().setMoveUp(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                            model.getP1().setMoveDown(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                                            model.getP1().setMoveLeft(false);
                                        }
                                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                                            model.getP1().setMoveRight(false);
                                        }
                                        if (e.getKeyChar() == 's') {
                                            model.getP1().setFire(false);
                                        }
                                    }
                                }
                            }
        );

    }
}