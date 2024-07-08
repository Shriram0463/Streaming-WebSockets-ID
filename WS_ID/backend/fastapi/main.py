from fastapi import FastAPI, WebSocket
import asyncio
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Load responses from file
responses = {}
with open('responses.txt', 'r') as file:
    for line in file:
        id, response = line.strip().split(',', 1)
        responses[id] = response

async def get_response(message_id: str) -> str:
    return responses.get(message_id, "Sorry, I don't have a response for that.")

@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    while True:
        message_id = await websocket.receive_text()
        response = await get_response(message_id)
        for char in response:
            await websocket.send_text(char)
            await asyncio.sleep(0.03)  # Adjust the sleep duration as needed