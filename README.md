# Real-Time Chat Application

A simple real-time chat application built using **Java, Spring Boot, WebSocket, STOMP, HTML, CSS, and JavaScript**.

The application allows multiple users to connect simultaneously and exchange messages in real time without refreshing the page.

## Features

- Real-time messaging using WebSocket
- STOMP protocol for message communication
- Multiple users can connect simultaneously
- Username-based chat
- Real-time message broadcasting
- JOIN notifications when a user connects
- LEAVE notifications when a user disconnects
- Automatic scrolling to the latest message


## Project Structure

```text
real-time-chat/
│
├── src/
│   └── main/
│       └── java/
│           └── org/example/
│               ├── Main.java
│               ├── WebConfig.java
│               ├── ChatController.java
│               ├── ChatMessage.java
│               └── WebSocketEventListener.java
│
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── app.js
│
├── pom.xml
└── README.md
