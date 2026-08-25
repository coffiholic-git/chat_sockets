package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Scanner;

public class TestWebSocketClient extends WebSocketClient {

    private boolean stompConnected = false;

    public TestWebSocketClient(String username) throws Exception {
        super(new URI("ws://localhost:8080/ws"));
        this.username = username;
    }

    private String username;

    @Override
    public void onOpen(ServerHandshake handshake) {

        System.out.println("WebSocket CONNECTED!");

        send(
                "CONNECT\n" +
                        "accept-version:1.2\n" +
                        "host:localhost\n" +
                        "\n" +
                        "\u0000"
        );
    }

    @Override
    public void onMessage(String message) {

        if (message.startsWith("CONNECTED")) {

            stompConnected = true;

            System.out.println("STOMP CONNECTED!");

            // Subscribe to chat
            send(
                    "SUBSCRIBE\n" +
                            "id:sub-0\n" +
                            "destination:/topic/messages\n" +
                            "\n" +
                            "\u0000"
            );

            System.out.println("You can now type messages:");
        }

        else if (message.startsWith("MESSAGE")) {

            // Extract the JSON body
            int bodyStart = message.indexOf("\n\n");

            if (bodyStart != -1) {

                String body =
                        message.substring(bodyStart + 2)
                                .replace("\u0000", "");

                System.out.println("\n" + body);
                System.out.print("> ");
            }
        }
    }

    public void sendChatMessage(String text) {

        if (!stompConnected) {
            System.out.println("STOMP is not connected yet!");
            return;
        }

        String json =
                "{\"sender\":\"" +
                        username +
                        "\",\"content\":\"" +
                        text +
                        "\"}";

        send(
                "SEND\n" +
                        "destination:/app/chat\n" +
                        "content-type:application/json\n" +
                        "\n" +
                        json +
                        "\u0000"
        );
    }

    @Override
    public void onClose(
            int code,
            String reason,
            boolean remote) {

        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {

        System.out.println("ERROR: " + ex.getMessage());
    }

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        TestWebSocketClient client =
                new TestWebSocketClient(username);

        System.out.println("Connecting...");

        client.connect();

        // Give the WebSocket/STOMP connection time to establish
        Thread.sleep(2000);

        while (true) {

            System.out.print("> ");

            String message = scanner.nextLine();

            if (message.equalsIgnoreCase("exit")) {
                client.close();
                break;
            }

            client.sendChatMessage(message);
        }

        scanner.close();
    }
}