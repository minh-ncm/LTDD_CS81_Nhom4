package com.backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    // Retrieve data from database
    /*
    origin from: https://stackoverflow.com/questions/50109885/firestore-how-can-read-data-from-outside-void-oncomplete-methods
    How to use:
    DatabaseManagement databaseManagement = new DatabaseManagement();
    databaseManagement.getLatestPreview(new DatabaseManagement.firestoreCallback() {
                @Override
                public void onCallback(List<NewsPreview> list) {
                    // Do processing here
                }
            }, amount);
    */
    public interface firestoreCallback {
        void onCallback(List<NewsPreview> list);
    }
    public void getLatestPreviews(firestoreCallback callback, int amount){
        Query query = database.collection(pathNews).orderBy("createdDate").limit(amount);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    LinkedList<NewsPreview> previews = new LinkedList<>();
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        NewsPreview news = document.toObject(NewsPreview.class);
                        previews.add(news);
                    }
                    callback.onCallback(previews);
                }
                else {
                    Log.d("TEST", "Error");
                }
            }
        });
    }
    public void getPreviewsByType(firestoreCallback callback, String type, int amount){
        Query query = database.collection(pathNews).whereEqualTo("type", type)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .limit(amount);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    LinkedList<NewsPreview> previews = new LinkedList<>();
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        NewsPreview news = document.toObject(NewsPreview.class);
                        previews.add(news);
                        Log.d("TEST", news.getCreatedDate().toString());
                    }
                    callback.onCallback(previews);
                }
                else {
                    Log.d("TEST", "Error");
                }
            }
        });
    }
}
