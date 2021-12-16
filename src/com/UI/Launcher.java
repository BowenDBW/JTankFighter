package com.UI;

import com.client.ClientUnit.ClientView;
import com.server.ServerUnit.ServerView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>
 *
 * @author BowenDeng
 * @version 1.0
 * @date 2021/12/16 17:42
 **/
public class Launcher {

    private static final int width
            = GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDisplayMode().getWidth();
    private static final int height
            = GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDisplayMode().getHeight();

    public static void menuUI() {

        JFrame jf = new JFrame();
        jf.setLayout(new GridLayout(3, 1, 10, 10));
        jf.setBounds((int)(0.5D * (double)width - 400.0D),
                (int)(0.5D * (double)height - 300.0D), 400, 900);
        JButton setup = new JButton("建立主机");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new ServerView();
            }
        });
        JButton connect = new JButton("连接主机");
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new ClientView();
            }
        });
        JButton quit = new JButton("退出");

        jf.add(setup);
        jf.add(connect);
        jf.add(quit);

        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        Launcher.menuUI();
    }
}
