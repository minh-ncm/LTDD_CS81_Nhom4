package com.backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatabaseManagement {
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final String pathNews = "news";
    private final String pathUsers = "users";

    public void writeNewsToDatabase(News news){
        String docId = news.getAuthorUsername() + "#" + news.getTitle() + "/";
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
        dict.clear();
        DocumentReference contentsRef = docRef.collection("contents")
                .document("contents");
        for (int i = 0; i < news.getContent().size(); i++) {
            dict.put(Integer.toString(i), news.getContent().get(i));
        }
        contentsRef.set(dict);

        // Write images to database
        DocumentReference imagesRef = docRef.collection("images")
                .document("images");
        dict.clear();
        for(int i = 0; i < news.getImageURLs().size(); i++) {
            dict.put(Integer.toString(i), news.getImageURLs().get(i));
        }
        imagesRef.set(dict);
    }
    public void writeUserToDatabase(User user) {
        DocumentReference docRef = database.collection(pathUsers).document(user.getUsername());

        Map<String, Object> dict = new HashMap<>();
        dict.put("username", user.getUsername());
        dict.put("password", user.getPassword());
        dict.put("isAdmin", user.isAdmin());
        dict.put("isWriter", user.isWriter());
        docRef.set(dict);
    }

//    Retrieve data from database
//    origin from: https://stackoverflow.com/questions/50109885/firestore-how-can-read-data-from-outside-void-oncomplete-methods
    public interface newsPreviewsCallback {
        /*
        Example:
        DatabaseManagement databaseManagement = new DatabaseManagement();
        databaseManagement.getLatestPreview(new DatabaseManagement.newsPreviewsCallback() {
                    @Override
                    public void onCallback(List<NewsPreview> list) {
                        // Do processing here
                    }
                }, amount);
        */
        void onCallback(List<NewsPreview> list);
    }
    public void getLatestPreviews(newsPreviewsCallback callback, int amount){
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
                    Log.d("_out", "Error");
                }
            }
        });
    }
    public void getPreviewsByType(newsPreviewsCallback callback, String type, int amount){
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
                        Log.d("_out", news.getCreatedDate().toString());
                    }
                    callback.onCallback(previews);
                }
                else {
                    Log.d("_out", "Error");
                }
            }
        });
    }

    public interface newsContentsCallback {
        void onCallback(List<String> contents);
    }

    // TODO:
    public void getNewsContents(newsContentsCallback callback, String title, String authorUsername){
        String newsId = authorUsername + "#" + title;
        DocumentReference newsRef = database.document(newsId);
        Query queryContents = newsRef.collection("contents");
        Query queryImages = newsRef.collection("images");
        Task contentsTask = queryContents.get();
        Task imagesTask = queryImages.get();

        Task combinedTask = Tasks.whenAllComplete(contentsTask, imagesTask)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        task.isSuccessful();
                        task.getResult();
                    }
                });

//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()) {
//                    News news = new News();
//                    for(QueryDocumentSnapshot document : task.getResult()) {
//                        news.setTitle(document.getString("title"));
//                        news.setAuthorUsername(document.getString("authorUsername"));
//                        news.setType(document.getString("type"));
//                        news.setCreatedDate(document.getTimestamp("createdDate").toDate());
////                        Log.d("_out", images.get().getResult().getData().toString());
//                    }
//                } else Log.d("_out", "Not found!");
//            }
//        });
    }

    public interface userCallback {
        /*
        Example:
        DatabaseManagement databaseManagement = new DatabaseManagement();
        databaseManagement.getUserFromDatabase(new DatabaseManagement.userCallback() {
                    @Override
                    public void onCallback(User user) {
                        // Do processing here
                    }
        }, username);
        */
        void onCallback(User user);
    }
    public void getUserFromDatabase(userCallback callback, String username) {
        DocumentReference docRef = database.collection(pathUsers).document(username);
        User user = new User();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("_out", "found");
                        user.setUsername(document.getString("username"));
                        user.setPassword(document.getString("password"));
                    }
                    else
                        Log.d("_out", "not found");
                    callback.onCallback(user);
                }
                else
                    Log.d("_out", "failed");
            }
        });
    }
    public void changeUserpassword (String username, String pwd){
        DocumentReference docRef = database.collection(pathUsers).document(username);
        docRef.update("password", pwd);
    }
}
