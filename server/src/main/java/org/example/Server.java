package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ConnectionServer> activeConnection = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server started");
        try {
            while (true) {
                Socket socket = server.accept();

                try {
                    ConnectionServer connection = new ConnectionServer(socket);
                    activeConnection.add(connection);
                    System.out.println("[SERVER] " + connection.getNameConnection() + " connected successfully");

                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}

//+ connection.getNameConnection() +