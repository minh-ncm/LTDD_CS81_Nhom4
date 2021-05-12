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

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatabaseManagement {
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final String pathNews = "news";
    private final String pathUsers = "users";
    private final String pathComments = "comments";

    public void writeNewsToDatabase(News news){
        DocumentReference docRef = database.collection(pathNews)
                .document(news.getAuthorUsername() + "#" + news.getTitle());

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
        dict.put("total", news.getContent().size());
        for (int i = 0; i < news.getContent().size(); i++) {
            dict.put(Integer.toString(i), news.getContent().get(i));
        }
        contentsRef.set(dict);

        // Write images to database
        dict.clear();
        DocumentReference imagesRef = docRef.collection("images")
                .document("images");
        dict.put("total", news.getImageURLs().size());
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
    public void writeCommentToDatabase(Comment comments) {
        database.collection(pathComments).document().set(comments);
    }

//    Retrieve data from database
//    origin from: https://stackoverflow.com/questions/50109885/firestore-how-can-read-data-from-outside-void-oncomplete-methods
    public interface newsListPreviewsCallback {
        /*
        Example:
        DatabaseManagement databaseManagement = new DatabaseManagement();
        databaseManagement.getLatestPreview(new DatabaseManagement.newsListPreviewsCallback() {
                    @Override
                    public void onCallback(List<NewsPreview> list) {
                        // Do processing here
                    }
                }, amount);
        */
        void onCallback(List<NewsPreview> list);
    }
    public void getLatestPreviews(newsListPreviewsCallback callback, int amount){
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
    public void getPreviewsByType(newsListPreviewsCallback callback, String type, int amount){
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
                    }
                    callback.onCallback(previews);
                }
                else {
                    Log.d("_out", "Error");
                }
            }
        });
    }

    public interface newsPreviewCallback{
        void onCallback(NewsPreview news);
    }
    public void getSpecificNewsPreview(newsPreviewCallback callback, String author, String title) {
        String docID = author + "#" + title;
        database.collection(pathNews)
                .document(docID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    NewsPreview news = doc.toObject(NewsPreview.class);
                    callback.onCallback(news);
                } else
                    Log.d("_out", "error when getting news");
            }
        });
    }

    public interface newsContentsCallback {
        void onCallback(List<String> contents);
    }
    public void getNewsContents(newsContentsCallback callback, String authorUsername, String title){
        String newsId = authorUsername + "#" + title;
        database.collection(pathNews)
                .document(newsId)
                .collection("contents")
                .document("contents")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    List<String> contents = new LinkedList<>();
                    DocumentSnapshot document = task.getResult();
                    int total = document.getLong("total").intValue();
                    for(int i = 0; i < total; i++) {
                        contents.add(document.getString(Integer.toString(i)));
                    }
                    callback.onCallback(contents);
                }
                else
                    Log.d("_out", "error when get contents");
            }
        });
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
    public void getUserData(userCallback callback, String username) {
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

    public interface commentsListCallback {
        void onCallback(List<Comment> comments);
    }
    public void getCommentsOfNews(commentsListCallback callback, String authorUsername, String title) {
        CollectionReference collection = database.collection(pathComments);
        collection
                .whereEqualTo("title", title)
                .whereEqualTo("authorUsername", authorUsername)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Comment> list = new LinkedList<>();
                    for (QueryDocumentSnapshot document :  task.getResult()) {
                        Comment comment = document.toObject(Comment.class);
                        list.add(comment);
                    }
                    callback.onCallback(list);
                }
            }
        });
    }

    public void changeUserPassword (String username, String pwd){
        DocumentReference docRef = database.collection(pathUsers).document(username);
        docRef.update("password", pwd);
    }
    public void deleteNews(String username, String title) {
        DocumentReference docRef = database.collection(pathNews).document(username + "#" + title);
        CollectionReference collection = database.collection(pathNews);
        collection
                .whereEqualTo("authorUsername", username)
                .whereEqualTo("title", title)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DocumentReference docRef = collection.document(document.getId());
                    docRef.collection("contents").document("contents").delete();
                    docRef.collection("images").document("images").delete();
                    docRef.delete();
                }
            }
        });
    }
}