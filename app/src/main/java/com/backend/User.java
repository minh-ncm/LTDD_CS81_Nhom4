package com.backend;

public class User {
    private String username;
    private String password;
    private boolean isWriter;
    private boolean isAdmin;

    // Getter
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isWriter() { return isWriter; }
    public boolean isAdmin() { return isAdmin; }

    // Setter
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setWriter(boolean writer) { isWriter = writer; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
}
