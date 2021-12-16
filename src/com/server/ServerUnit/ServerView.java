package com.server.ServerUnit;

import javax.swing.*;
import java.awt.*;


/**
 * @author chenhong
 */ //这个类表示服务器的图形界面
public class ServerView extends JFrame {

    private DrawingPanel mainPanel;
    private JButton createServer, exit, pauseAndResume, help;
    private JTextField messageField;
    private JButton sendMessage;
    private ServerController controller;
    private ServerModel model;

    public DrawingPanel getMainPanel() {
        return mainPanel;
    }

    public JButton getCreateServer() {
        return createServer;
    }

    public JButton getExit() {
        return exit;
    }

    public JButton getPauseAndResume() {
        return pauseAndResume;
    }


    public JButton getHelp() {
        return help;
    }


    public JTextField getMessageField() {
        return messageField;
    }

    public JButton getSendMessage() {
        return sendMessage;
    }

    public ServerView() {

        super("坦克大战");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

            e.printStackTrace();
        }

        getContentPane().setLayout(null);

        //制作动画绘制的主面板
        mainPanel = new DrawingPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 22, 679, 605);
        mainPanel.setBackground(new Color(128, 64, 0));

        messageField = new JTextField();
        messageField.setBounds(0, 519, 560, 22);
        messageField.setEnabled(false);
        sendMessage = new JButton("发送");
        sendMessage.setBounds(570, 518, 62, 24);
        sendMessage.setFocusable(false);
        mainPanel.add(messageField);
        mainPanel.add(sendMessage);
        getContentPane().add(mainPanel);
        mainPanel.setFocusable(true);

        //制作选项按钮
        createServer = new JButton("建立主机");
        createServer.setBounds(0, 0, 120, 22);
        getContentPane().add(createServer);
        createServer.setFocusable(false);

        pauseAndResume = new JButton("暂停/继续");
        pauseAndResume.setBounds(120, 0, 120, 22);
        getContentPane().add(pauseAndResume);
        pauseAndResume.setFocusable(false);

        help = new JButton("帮助");
        help.setBounds(240, 0, 120, 22);
        getContentPane().add(help);
        help.setFocusable(false);

        exit = new JButton("退出");
        exit.setBounds(360, 0, 120, 22);
        getContentPane().add(exit);
        exit.setFocusable(false);

        //设置主框架
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(150, 130, 640, 590);
        setVisible(true);
        setResizable(false);

        //设置服务器模式
        model = new ServerModel(this);

        //设置服务器控制器
        controller = new ServerController(this, model);
    }
}