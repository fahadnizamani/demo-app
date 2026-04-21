package com.example.demo.kafka;

public class UserRegisteredEvent {

    private String email;
    private String username;
    private String eventType;

    public UserRegisteredEvent() {}

    public UserRegisteredEvent(String email, String username, String eventType) {
        this.email = email;
        this.username = username;
        this.eventType = eventType;
    }

    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getEventType() { return eventType; }
}