package org.example;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ConnectionServer extends Thread {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String nameConnection;
    private LocalDateTime connectionTime;
    int count = 1;

    public ConnectionServer(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
        this.nameConnection = " client- " + count;
        count++;
        this.connectionTime = LocalDateTime.now();

    }

    @Override
    public void run() {
        String word;
        try {
            word = in.readLine();
            try {
                out.write(word + "\n");
                out.flush();
            } catch (IOException ignored) {
            }
            try {
                while (true) {
                    word = in.readLine();
                    if (word.equals("exit")) {
                        this.downService();
                        break;
                    }
                    System.out.println("Echoing: " + word);
                    for (ConnectionServer connection : Server.activeConnection) {
                        connection.send(word);
                    }
                }
            } catch (NullPointerException ignored) {
            }


        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }

    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ConnectionServer connection : Server.activeConnection) {
                    if (connection.equals(this)) connection.interrupt();
                    Server.activeConnection.remove(this);
                }
            }
        } catch (IOException ignored) {
        }
    }

    public String getNameConnection() {
        return nameConnection;
    }
}

//