package com.backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.perfmark.Link;

public class News {
    static public String[] typeNames = {"news", "sports", "laws", "businesses", "entertainments",
            "educations", "life", "heath", "world"};

    protected String title;
    protected Date createdDate = new Date();
    private List<String> contents = new LinkedList<>();
    protected String type;
    protected String authorUsername;
    private List<String> imageURLs = new LinkedList<>();

    // Constructor
    public News(String title, List<String> contents, String type, String authorUsername, List<String> imageURLs) {
        this.title = title;
        this.contents = contents;
        this.type = type;
        this.authorUsername = authorUsername;
        this.imageURLs = imageURLs;
    }

    public News() {
    }

    // Getter
    public String getTitle() { return title; }
    public Date getCreatedDate() { return createdDate; }
    public String getType() { return type; }
    public String getAuthorUsername() { return authorUsername; }
    public List<String> getImageURLs() { return imageURLs; }
    public List<String> getContent() { return contents; }

    // Setter
    public void setTitle(String title) { this.title = title; }
    public void setContents(List<String> contents) { this.contents = contents; }
    public void addContent(String content) { this.contents.add(content); }
    public void addImageURL(String url) { this.imageURLs.add(url); }
    public void setImageURLs(List<String> imageURLs) { this.imageURLs = imageURLs; }
    public void setType(String type) { this.type = type; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    //
    static public String createSampleContent(int min, int max) {
        Lorem lorem = LoremIpsum.getInstance();
        return lorem.getParagraphs(min, max);
    }
}