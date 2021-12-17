package com.client.ClientUnit;//	坦克大战连线版用户端

import com.client.ClientUnit.DrawingPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author chenhong
 */
public class ClientView extends JFrame {
    private final DrawingPanel mainPanel;
    private final JButton sendMessage;
    private final JButton connectServer;
    private final JButton exit;
    private final JButton pauseAndResume;
    private JButton help;
    private final JTextField messageField;
    private final JTextField ipField;


    public DrawingPanel getMainPanel() {
        return mainPanel;
    }


    public JButton getSendMessage() {
        return sendMessage;
    }


    public JButton getConnectServer() {
        return connectServer;
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

    public void setHelp(JButton help) {
        this.help = help;
    }

    public JTextField getMessageField() {
        return messageField;
    }


    public JTextField getIpField() {
        return ipField;
    }



    public ClientView() {
        super("坦克大战");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

            e.printStackTrace();
        }

        getContentPane().setLayout(null);

        //设置动画绘制主面板
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

        //设置选项按钮和IP文本字段
        JLabel enterIp = new JLabel("输入主机IP");
        enterIp.setBounds(10, 0, 60, 22);
        getContentPane().add(enterIp);

        ipField = new JTextField();
        ipField.setBounds(65, 0, 90, 22);
        getContentPane().add(ipField);

        connectServer = new JButton("连接主机");
        connectServer.setBounds(160, 0, 100, 22);
        getContentPane().add(connectServer);
        connectServer.setFocusable(false);

        pauseAndResume = new JButton("暂停/继续");
        pauseAndResume.setBounds(260, 0, 100, 22);
        getContentPane().add(pauseAndResume);
        pauseAndResume.setFocusable(false);

        help = new JButton("帮助");
        help.setBounds(360, 0, 100, 22);
        getContentPane().add(help);
        help.setFocusable(false);

        exit = new JButton("退出");
        exit.setBounds(460, 0, 100, 22);
        getContentPane().add(exit);
        exit.setFocusable(false);

        //设置面框架
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(150, 130, 640, 590);
        setVisible(true);
        setResizable(false);

        //设置客户端模型
        ClientModel model = new ClientModel(this);

        //设置客户端控制器
        ClientController controller = new ClientController(this, model);
    }
}