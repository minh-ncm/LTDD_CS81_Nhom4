package com.backend;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class News {
    static public String[] typeNames = {"news", "sports", "laws", "businesses", "entertainments",
            "educations", "life", "heath", "world"};

    private String title;
    private Date writeDate;
    private String content;
    private String type;
    private String authorUsername;

    // Constructor
    public News(String title, String content, String type) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.writeDate = new Date();
    }

    public News() {
    }

    // Getter
    public String getTitle() {
        return title;
    }
    public Date getWriteDate() {
        return writeDate;
    }
    public String getContent() {
        return content;
    }
    public String getType() {
        return type;
    }
    public String getAuthorUsername() {
        return authorUsername;
    }

    // Setter
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    //
    static public String createSampleContent(int min, int max) {
        Lorem lorem = LoremIpsum.getInstance();
        return lorem.getParagraphs(min, max);
    }

    // Public methods
    public String getPreviewContent(int endIndex){
        return content.substring(0, endIndex);
    }
}
