package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket socket;
    public List<ClientThread> clientList = new ArrayList<>();

    public Server(int port) throws IOException {
        this.socket = new ServerSocket(port);
    }

    public void listen() throws IOException {
        while(true) {
            Socket client = socket.accept();
            ClientThread thread = new ClientThread(client, this);
            clientList.add(thread);
            thread.start();
        }
    }

    public void broadcastSend(Message message) throws JsonProcessingException {
        for (ClientThread user :
             clientList) {
            user.send(message);
        }
    }
}
