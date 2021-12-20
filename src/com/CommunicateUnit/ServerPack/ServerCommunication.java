package com.CommunicateUnit.ServerPack;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 *
 * @author BowenDeng
 * @version 1.0
 * @date 2021 /12/19 14:05
 */
public class ServerCommunication {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    /**
     * Gets server socket.
     * 获取serverSocket
     * @return the server socket
     */
    public static ServerSocket getServerSocket() {

        return serverSocket;
    }

    /**
     * Gets client socket.
     * 获取clientSocket
     * @return the client socket
     */
    public static Socket getClientSocket() {

        return clientSocket;
    }

    /**
     * Gets out.
     * 获取流
     * @return the out
     */
    public static PrintWriter getOut() {

        return out;
    }

    /**
     * Gets in.
     * 获取流
     * @return the in
     */
    public static BufferedReader getIn() {

        return in;
    }

    /**
     * Sets server socket.
     * 设置socket
     * @param serverSocket the server socket
     */
    public static void setServerSocket(ServerSocket serverSocket) {

        ServerCommunication.serverSocket = serverSocket;
    }

    /**
     * Sets client socket.
     * 设设置socket
     * @param clientSocket the client socket
     */
    public static void setClientSocket(Socket clientSocket) {

        ServerCommunication.clientSocket = clientSocket;
    }

    /**
     * Sets out.
     * 设置流
     * @param out the out
     */
    public static void setOut(PrintWriter out) {

        ServerCommunication.out = out;
    }

    /**
     * Sets in.
     * 设置流
     * @param in the in
     */
    public static void setIn(BufferedReader in) {

        ServerCommunication.in = in;
    }
}
