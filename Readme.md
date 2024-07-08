# WebSocket Token Streaming Application

This project demonstrates a streaming application using FastAPI, Spring Boot, and React with WebSockets. The user sends a message from the frontend, which Spring Boot receives. Spring Boot checks the message in messages.txt, retrieves the corresponding ID, and sends the ID to FastAPI. FastAPI checks responses.txt for the received ID and sends the corresponding response back to Spring Boot. Spring Boot then streams the response letter by letter to React, which displays it to the user.


## Project Structure

websocket-token-streaming/
├── backend/
│   ├── springboot/
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/com/example/websocket/
│   │   │   │   │   ├── WebSocketHandler.java
│   │   │   │   └── resources/
│   │   │   │       ├── messages.txt
│   └── fastapi/
│       ├── main.py
│       ├── responses.txt
├── frontend/
│   ├── websocket-client/
│   │   ├── src/
│   │   │   ├── App.js
│   │   ├── public/
│   │   │   └── index.html
│   ├── package.json
│   ├── package-lock.json
├── README.md


## Features

1. **WebSocket Communication**: Real-time communication between frontend and backend using WebSockets.

2. **Message ID Lookup**: Spring Boot looks up message IDs from messages.txt.

3. **Response Retrieval**: FastAPI retrieves responses based on IDs from responses.txt.

4. **Letter by Letter Streaming**: Responses are streamed letter by letter to the frontend.

5.** React Frontend**: Interactive user interface to send messages and display responses.


## Prerequisites

Java Development Kit (JDK)

Python 3.7+

Node.js and npm


## Usage
1. Open the React app in your browser at http://localhost:3000.

2. Type a message in the input field and press Enter or click the Send button.

3. The response will be displayed letter by letter in real-time.


## File Overview

### Backend

#### Spring Boot

**WebSocketHandler.java**: Handles WebSocket connections and message processing.

**messages.txt**: Contains predefined messages with corresponding IDs.

#### FastAPI

**main.py**: FastAPI server handling WebSocket connections and message processing.

**responses.txt**: Contains responses mapped to message IDs.

### Frontend

#### React App

**App.js**: Main React component handling user input, WebSocket communication, and displaying responses.

**index.html**: HTML template for the React app.


## License

This project is licensed under the MIT License.
