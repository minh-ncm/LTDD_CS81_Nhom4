package com.backend;

public class Comment {
    protected String authorUsername;
    protected String title;
    protected String username;
    protected String comment;
    protected int upvote = 0;
    protected int downvote = 0;

    // Constructor
    public Comment(String username, String comment, String authorUsername, String title) {
        this.username = username;
        this.comment = comment;
        this.title = title;
        this.authorUsername = authorUsername;
    }
    public Comment(){}

    // Setter
    public void setUsername(String username) { this.username = username; }
    public void setComment(String comment) { this.comment = comment; }
    public void setUpvote(int upvote) { this.upvote = upvote; }
    public void setDownvote(int downvote) { this.downvote = downvote; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }


    // Getter
    public String getUsername() { return username; }
    public String getComment() { return comment; }
    public int getUpvote() { return upvote; }
    public int getDownvote() { return downvote; }
    public String getTitle() { return title; }
    public String getAuthorUsername() { return authorUsername; }
}
