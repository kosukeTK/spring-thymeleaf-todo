package com.kosuke.user.email;

public interface EmailSender {
	
    void send(String to, String email);
    
    String buildEmail(String username, String link);
}
