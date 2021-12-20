package com.CommunicateUnit.ClientPack;

import com.UI.ClientDrawingPanel;
import com.ProcessUnit.ClientPack.ClientModel;
import com.ProcessUnit.ClientPack.ClientStatus;
import com.UI.ClientView;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 这个类处理来自客户端视图框架的输入
 * @author chenhong
 */
public class ClientController {

    private final ClientView view;
    private int helpMessageCount = 1;

    public ClientController(ClientView thisView) {
        view = thisView;

        //发送消息按钮操作
        view.getSendMessage().addActionListener(e -> {
                    if (!ClientStatus.isGameStarted()) {
                        ClientDrawingPanel.addMessage("还没有和主机端玩家联上, 无法发送对话");
                        return;
                    }

                    if (!"".equals(view.getMessageField().getText())) {
                        ClientDrawingPanel.addMessage("用户端玩家说：" +
                                view.getMessageField().getText());

                        ClientModel.playerTypedMessage += "e" +
                                view.getMessageField().getText() + ";";

                        view.getMessageField().setText("");
                    } else {
                        ClientDrawingPanel.addMessage("对话内容不能为空");
                    }
                }
        );

        //handle connectServer按钮操作  点击连接主机的按钮
        view.getConnectServer().addActionListener(e -> {
                    if (ClientStatus.isServerConnected()) {
                        ClientCommunication.setServerIP(view.getIpField().getText());
                        ClientModel.getT().start();
                    }
                }
        );

        //handle pauseAndResume 按钮操作
        view.getPauseAndResume().addActionListener(e -> {
                    if (!ClientStatus.isGameOver() && ClientStatus.isGameStarted()) {
                        ClientStatus.setPausePressed(true);
                        if (!ClientStatus.isGamePaused()) {
                            ClientStatus.setGamePaused(true);
                            ClientDrawingPanel.addMessage("用户端玩家暂停了游戏");
                        } else {
                            ClientStatus.setGamePaused(false);
                            ClientDrawingPanel.addMessage("用户端玩家取消了暂停");
                        }
                    }
                }
        );

        //handle help 按钮操作
        view.getHelp().addActionListener(e -> {
            ClientDrawingPanel.addMessage("******************************坦克大战 ******************************");
                    ClientDrawingPanel.addMessage("帮助: 按s键发射子弹,按键盘的方向键来控制坦克的移动");
                    ClientDrawingPanel.addMessage("如果按键没有反应请 1. 关闭大写功能; 2. 用 tab键切换 ");
                    ClientDrawingPanel.addMessage("如果您在使用对话界面请移动到控制界面.");
                    ClientDrawingPanel.addMessage("********************************************************************************");
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
                    ClientDrawingPanel.addMessage("提示：用\"tab\"键可以自由切换于控制界面和对话界面");
                    ClientDrawingPanel.addMessage("提示：按回车键可以直接发送您的对话");
                    helpMessageCount--;
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!"".equals(view.getMessageField().getText())) {
                        ClientDrawingPanel.addMessage("用户端玩家说：" +
                                view.getMessageField().getText());
                        ClientModel.playerTypedMessage += "e" +
                                view.getMessageField().getText() + ";";
                        view.getMessageField().setText("");
                    } else {
                        ClientDrawingPanel.addMessage("对话内容不能为空");
                    }
                }
            }
        });

        JPanel temp = view.getMainPanel();
        temp.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            ClientStatus.setMoveUp(true);
                            ClientStatus.setMoveDown(false);
                            ClientStatus.setMoveLeft(false);
                            ClientStatus.setMoveRight(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            ClientStatus.setMoveDown(true);
                            ClientStatus.setMoveUp(false);
                            ClientStatus.setMoveLeft(false);
                            ClientStatus.setMoveRight(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            ClientStatus.setMoveLeft(true);
                            ClientStatus.setMoveUp(false);
                            ClientStatus.setMoveDown(false);
                            ClientStatus.setMoveRight(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            ClientStatus.setMoveLeft(false);
                            ClientStatus.setMoveUp(false);
                            ClientStatus.setMoveDown(false);
                            ClientStatus.setMoveRight(true);
                        }

                        if (e.getKeyChar() == 's') {
                            ClientStatus.setFire(true);
                        }

                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (!"".equals(view.getMessageField().getText())) {
                                ClientDrawingPanel.addMessage("用户端玩家说：" +
                                        view.getMessageField().getText());
                                ClientModel.playerTypedMessage += "e" +
                                        view.getMessageField().getText() + ";";
                                view.getMessageField().setText("");
                            }
                        }

                        if (e.getKeyChar() == 'y' && ClientStatus.isGameOver()
                                && !ClientStatus.isClientVoteYes()) {
                            ClientStatus.setClientVoteYes(true);
                            ClientDrawingPanel.addMessage("等待主机端玩家回应...");
                        }

                        if (e.getKeyChar() == 'n' && ClientStatus.isGameOver()) {
                            ClientStatus.setClientVoteNo(true);
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            ClientStatus.setMoveUp(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            ClientStatus.setMoveDown(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            ClientStatus.setMoveLeft(false);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            ClientStatus.setMoveRight(false);
                        }
                        if (e.getKeyChar() == 's') {
                            ClientStatus.setFire(false);
                        }
                    }
                }
        );
    }
}

