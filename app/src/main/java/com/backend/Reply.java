package com.backend;

public class Reply  extends  Comment{
    private String repliedUsername;

    // Constructor

    public Reply(String username, String comment, String title, String authorUsername, String repliedUsername) {
        super(username, comment, title, authorUsername);
        this.repliedUsername = repliedUsername;
    }

    public Reply(String repliedUsername) { this.repliedUsername = repliedUsername; }

    // Getter
    public String getRepliedUsername() { return repliedUsername; }

    // Setter
    public void setRepliedUsername(String repliedUsername) { this.repliedUsername = repliedUsername; }
}
