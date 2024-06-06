package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread{
    Socket client;
    Server server;
    PrintWriter writer;

    public ClientThread(Socket client, Server server) {
        this.client = client;
        this.server = server;
    }
    public void run() {
        try {
            InputStream input = client.getInputStream();
            OutputStream output = client.getOutputStream();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input)
            );
            this.writer = new PrintWriter(output, true);

            String rawMessage;

            while((rawMessage = reader.readLine()) != null) {
                Message message = new ObjectMapper()
                        .readValue(rawMessage, Message.class);

                switch (message.type) {
                    case Broadcast -> server.broadcastSend(message);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Message message) throws JsonProcessingException {
        String rawMessage = new ObjectMapper().writeValueAsString(message);
        writer.println(rawMessage);
    }
}
