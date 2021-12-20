package com.UI;

import com.CommunicateUnit.ServerPack.ServerController;
import com.ProcessUnit.ServerPack.ServerModel;
import com.UI.ServerDrawingPanel;

import javax.swing.*;
import java.awt.*;


/**
 * The type Server view.
 * 这个类表示服务器的图形界面
 * @author chenhong
 */
public class ServerView extends JFrame {

    private final ServerDrawingPanel mainPanel;
    private final JButton createServer;
    private final JButton exit;
    private final JButton pauseAndResume;
    private final JButton help;
    private final JTextField messageField;
    private final JButton sendMessage;

    /**
     * Gets main panel.
     * 获取主面板
     * @return the main panel
     */
    public ServerDrawingPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Gets create server.
     * 获取按钮
     * @return the create server
     */
    public JButton getCreateServer() {
        return createServer;
    }

    /**
     * Gets exit.
     * 获取按钮
     * @return the exit
     */
    public JButton getExit() {
        return exit;
    }

    /**
     * Gets pause and resume.
     * 获取按钮
     * @return the pause and resume
     */
    public JButton getPauseAndResume() {
        return pauseAndResume;
    }


    /**
     * Gets help.
     * 获取帮助
     * @return the help
     */
    public JButton getHelp() {
        return help;
    }


    /**
     * Gets message field.
     * 获取文本框
     * @return the message field
     */
    public JTextField getMessageField() {
        return messageField;
    }

    /**
     * Gets send message.
     * 获取按钮
     * @return the send message
     */
    public JButton getSendMessage() {
        return sendMessage;
    }

    /**
     * Instantiates a new Server view.
     * 安排各个组件的布局以及大小， 初始化监听
     */
    public ServerView() {

        super("坦克大战");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

            e.printStackTrace();
        }

        getContentPane().setLayout(null);

        //制作动画绘制的主面板
        mainPanel = new ServerDrawingPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 22, 679, 605);
        mainPanel.setBackground(new Color(128, 64, 0));

        messageField = new JTextField();
        messageField.setBounds(0, 507, 560, 22);
        messageField.setEnabled(false);
        sendMessage = new JButton("发送");
        sendMessage.setBounds(562, 507, 62, 24);
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
        ServerModel model = new ServerModel(this);

        //设置服务器控制器
        new ServerController(this);
    }
}