package com.CommunicateUnit.ServerPack;

import com.ProcessUnit.ServerPack.ServerModel;
import com.ProcessUnit.ServerPack.ServerStatus;
import com.UI.ServerDrawingPanel;
import com.UI.ServerView;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * The type Server controller.
 * 主要设置来自面板的各个按钮的事件监听
 * @author chenhong
 */
public class ServerController {
    private final ServerView view;
    private int helpMessageCount = 1;
    //一个玩家坦克的参考

    /**
     * Instantiates a new Server controller.
     * 初始化并且，设置监听
     * @param thisView the view
     */
    public ServerController(ServerView thisView) {
        view = thisView;

        //操作发送消息按钮的动作
        view.getSendMessage().addActionListener(e -> {
            if (!ServerStatus.isGameStarted()) {
                ServerDrawingPanel.addMessage("还没有和别的玩家联上, 无法发送对话");
                return;
            }

            if (!"".equals(view.getMessageField().getText())) {

                ServerDrawingPanel.addMessage("主机端玩家说：" + view.getMessageField().getText());
                ServerModel.setPlayerTypedMessage(
                        ServerModel.getPlayerTypedMessage() + "m" + view.getMessageField().getText() + ";");
                view.getMessageField().setText("");
            } else {

                ServerDrawingPanel.addMessage("对话内容不能为空");
            }
        }
        );

        //操作建立主机按钮的动作
        view.getCreateServer().addActionListener(e -> {
            if (!ServerStatus.isServerCreated()) {
                ServerModel.getT().start();
            }
        }
        );

        //操作暂停/继续按钮的动作
        view.getPauseAndResume().addActionListener(e -> {
            ServerStatus.setPausePressed(true);
            if (!ServerStatus.isGameOver() && ServerStatus.isGameStarted()) {
                if (!ServerStatus.isGamePaused()) {
                    ServerStatus.setGamePaused(true);
                    ServerDrawingPanel.addMessage("主机端玩家暂停了游戏");
                } else {
                    ServerStatus.setGamePaused(false);
                    ServerDrawingPanel.addMessage("主机端玩家取消了暂停");
                }
            }
        }
        );

        //操作帮助按钮的动作
        view.getHelp().addActionListener(e -> {
                    ServerDrawingPanel.addMessage("-------------------------------坦克大战 1.0-----------------------------------");
                    ServerDrawingPanel.addMessage("帮助: 按 s 键开火,  按键盘的方向键来控制坦克的移动");
                    ServerDrawingPanel.addMessage("如果按键没有反应请 1. 关闭大写功能; 2. 用 tab键切换 ");
                    ServerDrawingPanel.addMessage("到控制界面如果您在使用对话界面.");
                    ServerDrawingPanel.addMessage("--------------------------------------------------------------------------------------");
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
                    ServerDrawingPanel.addMessage("提示：用\"tab\"键可以自由切换于控制界面和对话界面");
                    ServerDrawingPanel.addMessage("提示：按回车键可以直接发送您的对话");
                    helpMessageCount--;
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    if (!"".equals(view.getMessageField().getText())) {

                        ServerDrawingPanel.addMessage("主机端玩家说：" + view.getMessageField().getText());

                        ServerModel.setPlayerTypedMessage(ServerModel.getPlayerTypedMessage()
                                + "m" + view.getMessageField().getText() + ";");

                        view.getMessageField().setText("");
                    } else {

                        ServerDrawingPanel.addMessage("对话内容不能为空");
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
                                        char shot = 's';
                                        if (e.getKeyChar() == shot) {
                                            ServerModel.getP1().setFire(true);
                                        }

                                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                            if (!"".equals(view.getMessageField().getText())) {

                                                ServerDrawingPanel.addMessage("主机端玩家说：" + view.getMessageField().getText());

                                                ServerModel.setPlayerTypedMessage(ServerModel.getPlayerTypedMessage() +
                                                        "m" + view.getMessageField().getText() + ";");

                                                view.getMessageField().setText("");
                                            }
                                        }
                                        char yes = 'y';
                                        if (e.getKeyChar() == yes && ServerStatus.isGameOver() && !ServerStatus.isServerVoteYes()) {

                                            ServerStatus.setServerVoteYes(true);
                                            ServerDrawingPanel.addMessage("等待用户端玩家的回应...");
                                        }
                                        char no = 'n';
                                        if (e.getKeyChar() == no && ServerStatus.isGameOver()) {
                                            ServerStatus.setServerVoteNo(true);
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
                                        char shot = 's';
                                        if (e.getKeyChar() == shot) {
                                            ServerModel.getP1().setFire(false);
                                        }
                                    }
                                }
                            }
        );

    }
}