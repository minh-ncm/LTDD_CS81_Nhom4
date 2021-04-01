package com.backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

public class News {
    static public String[] typeNames = {"news", "sports", "laws", "businesses", "entertainments",
            "educations", "life", "heath", "world"};

    private String title;
    private Date createdDate = new Date();
    private List<String> contents = new LinkedList<>();
    private String type;
    private String authorUsername;
    private List<String> imageURLs = new LinkedList<>();
    private int previewLength = 100;

    // Constructor
    public News(String title, List<String> contents, String type, String authorUsername) {
        this.title = title;
        this.contents = contents;
        this.type = type;
        this.authorUsername = authorUsername;
    }

    public News() {
    }

    // Getter
    public String getTitle() { return title; }
    public Date getcreatedDate() { return createdDate; }
    public String getType() { return type; }
    public String getAuthorUsername() { return authorUsername; }
    public int getPreviewLength() { return previewLength; }
    public List<String> getImageURLs() { return imageURLs; }
    public String getThumbnailURL() { return imageURLs.get(0); }
    public List<String> getContent() { return contents; }
    public String getPreviewContent(){ return contents.get(0).substring(0, previewLength); }

    // Setter
    public void setTitle(String title) { this.title = title; }
    public void addContent(String content) { this.contents.add(content); }
    public void addImageURL(String url) { this.imageURLs.add(url); }
    public void setType(String type) { this.type = type; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
    public void setImageURLs(List<String> imageURLs) { this.imageURLs = imageURLs; }
    public void setPreviewLength(int previewLength) { this.previewLength = previewLength; }

    //
    static public String createSampleContent(int min, int max) {
        Lorem lorem = LoremIpsum.getInstance();
        return lorem.getParagraphs(min, max);
    }
    static public void getNewsByType(String type, int amount){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference newsRef = database.collection("news");
        Query query = newsRef.whereEqualTo("type", type).limit(amount);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TEST", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("TEST", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    // Public methods
    public void writeNewsToDatabase(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        StringBuilder path = new StringBuilder("news/");
        path.append(authorUsername).append("_").append(title).append("/");
        database.document(path.toString());
        DocumentReference docRef = database.document(path.toString());

        Map<String, Object> dict = new HashMap<>();
        dict.put("title", title);
        dict.put("authorUsername", authorUsername);
        dict.put("type", type);
        dict.put("preview", getPreviewContent());
        dict.put("thumbnail", imageURLs.get(0));
        dict.put("createdDate", createdDate);
        docRef.set(dict);

        // Write contents to database
        for (int i = 0; i < contents.size(); i++) {
            dict.clear();
            dict.put(Integer.toString(i), contents.get(i));
            docRef.collection("contents").document(Integer.toString(i)).set(dict);
        }

        // Write images to database
        for(int i = 0; i < imageURLs.size(); i++) {
            dict.clear();
            dict.put(Integer.toString(i), imageURLs.get(i));
            docRef.collection("images").document(Integer.toString(i)).set(dict);
        }
    }
    public void getNewsByAuthorAndTitle(String username, String title){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference article = database.collection("news");
        Query query = article.whereEqualTo("author", username).whereEqualTo("title", title).limit(1);
        for (QueryDocumentSnapshot document : query.get().getResult()) {
            Map<String, Object> dict = document.getData();
            this.title = (String)dict.get("title");
            this.authorUsername = (String)dict.get("authorUsername");
            this.createdDate = (Date) dict.get("createdDate");
        }
    }
}