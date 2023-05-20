package com.example.brawlheroes.Network;

public class Message {
    public static enum MessageType {
        MOVE, FIRE, CONNECT, DEATH, RESPAWN
    }
    private Object data;
    private MessageType type;
    public Message(Object data, MessageType type) {
        this.data = data;
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public MessageType getType() {
        return type;
    }
}
