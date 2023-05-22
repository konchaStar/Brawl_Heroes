package com.example.brawlheroes.Network;

import java.io.Serializable;

public class Message implements Serializable {
    public static enum MessageType {
        MOVE, FIRE, CONNECT, DEATH, RESPAWN, STARTED, TEST
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
