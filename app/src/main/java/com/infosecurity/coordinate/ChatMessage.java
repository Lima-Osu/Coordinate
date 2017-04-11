package com.infosecurity.coordinate;

import java.util.Date;

public class ChatMessage {
    private String message;
    private String user;
    private long messageTime;

    public ChatMessage(String message, String user){
        this.message = message;
        this.user = user;
        messageTime = new Date().getTime();
    }



}
