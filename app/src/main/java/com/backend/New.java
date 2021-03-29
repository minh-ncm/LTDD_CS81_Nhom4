package com.backend;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.io.FileNotFoundException;
import java.util.Date;

public class New {
    private String title;
    private Date writeDate;
    private String content;
    private String type;

    // Constructor
    public New(String title, String content) {
        this.title = title;
        this.content = content;
        this.type = "uncategorized";
        this.writeDate = new Date();
    }

    public New(String title, String content, String type) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.writeDate = new Date();
    }

    public New() {
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

    static public String createSampleContent() throws FileNotFoundException {
        Lorem lorem = LoremIpsum.getInstance();
        return "";
    }
}
