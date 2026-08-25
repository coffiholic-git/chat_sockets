let stompClient = null;

function connect() {

    const username =
        document.getElementById("username").value.trim();

    if (username === "") {
        alert("Enter a username");
        return;
    }

    stompClient = new StompJs.Client({

        brokerURL: "ws://localhost:8080/ws",

        reconnectDelay: 5000,

        onConnect: function () {

            console.log("Connected!");

            document.getElementById("status").innerText =
                "Connected as " + username;

            document.getElementById("message").disabled = false;
            document.getElementById("sendButton").disabled = false;

            // Subscribe FIRST
            stompClient.subscribe(
                "/topic/messages",
                function (message) {

                    const chatMessage =
                        JSON.parse(message.body);

                    displayMessage(chatMessage);
                }
            );

            // Tell Spring Boot that this user joined
            stompClient.publish({

                destination: "/app/chat.addUser",

                body: JSON.stringify({
                    sender: username,
                    content: "",
                    type: "JOIN"
                })
            });
        },

        onStompError: function (frame) {

            console.error("STOMP error:", frame);

            document.getElementById("status").innerText =
                "Connection error";
        },

        onWebSocketError: function (error) {

            console.error("WebSocket error:", error);

            document.getElementById("status").innerText =
                "WebSocket error";
        }
    });

    stompClient.activate();
}


function sendMessage() {

    const username =
        document.getElementById("username").value.trim();

    const messageInput =
        document.getElementById("message");

    const content =
        messageInput.value.trim();

    if (content === "") {
        return;
    }

    const chatMessage = {

        sender: username,

        content: content,

        type: "CHAT"
    };

    stompClient.publish({

        destination: "/app/chat",

        body: JSON.stringify(chatMessage)
    });

    messageInput.value = "";
}


function displayMessage(chatMessage) {

    const messages =
        document.getElementById("messages");

    const currentUser =
        document.getElementById("username").value.trim();

    const messageElement =
        document.createElement("div");


    // JOIN / LEAVE message
    if (
        chatMessage.type === "JOIN" ||
        chatMessage.type === "LEAVE"
    ) {

        messageElement.classList.add("system-message");

        messageElement.innerText =
            chatMessage.content;

    }

    // Normal chat message
    else {

        messageElement.classList.add("message");

        if (chatMessage.sender === currentUser) {

            messageElement.classList.add("my-message");

        } else {

            messageElement.classList.add("other-message");
        }

        messageElement.innerHTML = `
            <strong>${chatMessage.sender}</strong>
            <div>${chatMessage.content}</div>
        `;
    }

    messages.appendChild(messageElement);

    messages.scrollTop =
        messages.scrollHeight;
}


// Press Enter to send
document
    .getElementById("message")
    .addEventListener("keydown", function(event) {

        if (event.key === "Enter") {

            sendMessage();
        }
    });