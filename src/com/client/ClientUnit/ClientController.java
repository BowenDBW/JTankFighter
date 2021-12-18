package com.client.ClientUnit;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 这个类处理来自客户端视图框架的输入
 * @author chenhong
 */
public class ClientController {

    private final ClientView view;
    private final ClientModel model;
    private int helpMessageCount = 1;

    public ClientController(ClientView thisView, ClientModel thisModel) {
        view = thisView;
        model = thisModel;

        //发送消息按钮操作
        view.getSendMessage().addActionListener(e -> {
                    if (!Status.isGameStarted()) {
                        model.addMessage("还没有和主机端玩家联上, 无法发送对话");
                        return;
                    }

                    if (!"".equals(view.getMessageField().getText())) {
                        model.addMessage("用户端玩家说：" +
                                view.getMessageField().getText());

                        model.playerTypedMessage += "e" +
                                view.getMessageField().getText() + ";";

                        view.getMessageField().setText("");
                    } else {
                        model.addMessage("对话内容不能为空");
                    }
                }
        );

        //handle connectServer按钮操作  点击连接主机的按钮
        view.getConnectServer().addActionListener(e -> {
                    if (!Status.isServerConnected()) {
                        model.setServerIP(view.getIPField().getText());
                        model.getT().start();
                    }
                }
        );

        //handle pauseAndResume 按钮操作
        view.getPauseAndResume().addActionListener(e -> {
                    if (!Status.isGameOver() && Status.isGameStarted()) {
                        Status.setPausePressed(true);
                        if (!Status.isGamePaused()) {
                            Status.setGamePaused(true);
                            model.addMessage("用户端玩家暂停了游戏");
                        } else {
                            Status.setGamePaused(false);
                            model.addMessage("用户端玩家取消了暂停");
                        }
                    }
                }
        );

        //handle help 按钮操作
        view.getHelp().addActionListener(e -> {
                    model.addMessage("******************************坦克大战 ******************************");
                    model.addMessage("帮助: 按s键发射子弹,按键盘的方向键来控制坦克的移动");
                    model.addMessage("如果按键没有反应请 1. 关闭大写功能; 2. 用 tab键切换 ");
                    model.addMessage("如果您在使用对话界面请移动到控制界面.");
                    model.addMessage("********************************************************************************");
                }
        );

        //handle exit 按钮操作
        view.getExit().addActionListener(e -> System.exit(0)
        );

        //处理从键盘输入
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
                        model.addMessage("用户端玩家说：" +
                                view.getMessageField().getText());
                        model.playerTypedMessage += "e" +
                                view.getMessageField().getText() + ";";
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
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            Status.setMoveUp(true);
                            Status.setMoveDown(false);
                            Status.setMoveLeft(false);
                            Status.setMoveRight(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            Status.setMoveDown(true);
                            Status.setMoveUp(false);
                            Status.setMoveLeft(false);
                            Status.setMoveRight(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            Status.setMoveLeft(true);
                            Status.setMoveUp(false);
                            Status.setMoveDown(false);
                            Status.setMoveRight(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            Status.setMoveLeft(false);
                            Status.setMoveUp(false);
                            Status.setMoveDown(false);
                            Status.setMoveRight(true);
                        }

                        if (e.getKeyChar() == 's') {
                            Status.setFire(true);
                        }

                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (!"".equals(view.getMessageField().getText())) {
                                model.addMessage("用户端玩家说：" +
                                        view.getMessageField().getText());
                                model.playerTypedMessage += "e" +
                                        view.getMessageField().getText() + ";";
                                view.getMessageField().setText("");
                            }
                        }

                        if (e.getKeyChar() == 'y' && Status.isGameOver()
                                && !Status.isClientVoteYes()) {
                            Status.setClientVoteYes(true);
                            model.addMessage("等待主机端玩家回应...");
                        }

                        if (e.getKeyChar() == 'n' && Status.isGameOver()) {
                            Status.setClientVoteNo(true);
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            Status.setMoveUp(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            Status.setMoveDown(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            Status.setMoveLeft(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            Status.setMoveRight(false);
                        }
                        if (e.getKeyChar() == 's') {
                            Status.setFire(false);
                        }
                    }
                }
        );
    }
}

