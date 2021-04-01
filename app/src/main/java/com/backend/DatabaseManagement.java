package com.backend;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatabaseManagement {
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final String pathNews = "news";
    private final String pathUsers = "users";
    public void writeNewsToDatabase(News news){
        String docId = news.getAuthorUsername() + "_" + news.getTitle() + "/";
        DocumentReference docRef = database.collection(pathNews).document(docId);

        Map<String, Object> dict = new HashMap<>();
        dict.put("title", news.getTitle());
        dict.put("authorUsername", news.getAuthorUsername());
        dict.put("type", news.getType());
        dict.put("createdDate", news.getCreatedDate());
        dict.put("previewContent", news.getContent().get(0));
        dict.put("thumbnailURL", news.getImageURLs().get(0));
        docRef.set(dict);

        // Write contents to database
        for (int i = 0; i < news.getContent().size(); i++) {
            dict.clear();
            dict.put(Integer.toString(i), news.getContent().get(i));
            docRef.collection("contents").document(Integer.toString(i)).set(dict);
        }

        // Write images to database
        for(int i = 0; i < news.getImageURLs().size(); i++) {
            dict.clear();
            dict.put(Integer.toString(i), news.getImageURLs().get(i));
            docRef.collection("images").document(Integer.toString(i)).set(dict);
        }
    }
    public void getLatestPreview(int amount){
        List<NewsPreview> previews = new LinkedList<>();
        database.collection(pathNews).whereEqualTo("type", "laws");
    }
}
