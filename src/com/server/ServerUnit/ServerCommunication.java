package com.server.ServerUnit;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 *
 * @author BowenDeng
 * @version 1.0
 * @date 2021/12/19 14:05
 **/
public class ServerCommunication {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static Socket getClientSocket() {
        return clientSocket;
    }

    public static PrintWriter getOut() {
        return out;
    }

    public static BufferedReader getIn() {
        return in;
    }

    public static void setServerSocket(ServerSocket serverSocket) {
        ServerCommunication.serverSocket = serverSocket;
    }

    public static void setClientSocket(Socket clientSocket) {
        ServerCommunication.clientSocket = clientSocket;
    }

    public static void setOut(PrintWriter out) {
        ServerCommunication.out = out;
    }

    public static void setIn(BufferedReader in) {
        ServerCommunication.in = in;
    }

}
