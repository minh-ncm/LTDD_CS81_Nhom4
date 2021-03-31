package com.backend;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class News {
    static public String[] typeNames = {"news", "sports", "laws", "businesses", "entertainments",
            "educations", "life", "heath", "world"};

    private String title;
    private final Date writeDate = new Date();
    private String content;
    private String type;
    private String authorUsername;
    private List<String> imageURLs = new LinkedList<>();

    // Constructor
    public News(String title, String content, String type, String authorUsername) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.authorUsername = authorUsername;
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
    public List<String> getImageURLs() {
        return imageURLs;
    }
    public String getThumbnailURL() { return imageURLs.get(0); }

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
    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }
    public void setThumbnail(String url) { imageURLs.add(0, url); }

    //
    static public String createSampleContent(int min, int max) {
        Lorem lorem = LoremIpsum.getInstance();
        return lorem.getParagraphs(min, max);
    }

    // Public methods
    public String getPreviewContent(int endIndex){
        return content.substring(0, endIndex);
    }
    public void writeNewsToDatabase(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        StringBuilder path = new StringBuilder("news/");
        path.append(authorUsername).append("_").append(title).append("/");
        database.document(path.toString());
        DocumentReference docRef = database.document(path.toString());

        Map<String, String> newsPreview = new HashMap<>();
        newsPreview.put("title", title);
        newsPreview.put("author", authorUsername);
        newsPreview.put("type", type);
        newsPreview.put("preview", getPreviewContent(150));
        docRef.set(newsPreview);
        docRef.update("created date", writeDate);

        // Second level news database
        Map<String, String> contentFull = new HashMap<>();
        contentFull.put("full", content);
        docRef.collection("content").document("0").set(contentFull);
    }
    public void addPicURL(String url){ imageURLs.add(url); }
}