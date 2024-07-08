import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [ws, setWs] = useState(null);
  const [inputMessage, setInputMessage] = useState('');
  const [responseMessage, setResponseMessage] = useState('');

  useEffect(() => {
    const socket = new WebSocket("ws://localhost:8080/ws");

    socket.onopen = () => {
      console.log("Connected to WebSocket");
      setWs(socket);
    };

    socket.onmessage = (event) => {
      setResponseMessage(prevMessage => prevMessage + event.data);
    };

    socket.onclose = () => {
      console.log("Disconnected from WebSocket");
    };

    return () => {
      socket.close();
    };
  }, []);

  const sendMessage = () => {
    if (ws && inputMessage.trim() !== '') {
      ws.send(inputMessage);
      setInputMessage('');
      setResponseMessage('');
    }
  };

  const handleInputChange = (event) => {
    setInputMessage(event.target.value);
  };

  const handleKeyDown = (event) => {
    if (event.key === 'Enter') {
      sendMessage();
      event.preventDefault();
    }
  };

  return (
    <div className="App">
      <h1>WebSocket Token Streaming</h1>
      <input
        type="text"
        value={inputMessage}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown}
        placeholder="Type a message..."
      />
      <button onClick={sendMessage}>Send</button>
      <div id="response">
        <p>{responseMessage}</p>
      </div>
    </div>
  );
}

export default App;