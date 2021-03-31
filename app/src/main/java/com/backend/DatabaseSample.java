package com.backend;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DatabaseSample {
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private String getRandomType(String[] array){
        int index = new Random().nextInt(array.length);
        return array[index];
    }

    public News generateSampleNews() {
        Lorem lorem = new LoremIpsum();
        News sample = new News();
        sample.setTitle(lorem.getWords(3, 5));
        sample.setContent(lorem.getParagraphs(5, 6));
        sample.setType(getRandomType(News.typeNames));
        sample.setAuthorUsername("admin");
        return sample;
    }

    private void writeOneNewToDataBase(){
        StringBuilder path = new StringBuilder("news/");
        News sample = generateSampleNews();
        // Preview level news database
        path.append(sample.getAuthorUsername()).append("_").append(sample.getTitle()).append("/");
        database.document(path.toString());
        DocumentReference docRef = database.document(path.toString());

        Map<String, String> newsPreview = new HashMap<>();
        newsPreview.put("title", sample.getTitle());
        newsPreview.put("author", sample.getAuthorUsername());
        newsPreview.put("type", sample.getType());
        newsPreview.put("preview", sample.getPreviewContent(150));
        docRef.set(newsPreview);
        docRef.update("created date", sample.getWriteDate());

        // Second level news database
        Map<String, String> content = new HashMap<>();
        content.put("full", sample.getContent());
        docRef.collection("content").document("0").set(content);
    }

    public void writeToNewsDatabase(int amount){
        for(int i = 1; i <= amount; i++)
            writeOneNewToDataBase();
    }

    public void writeToUsersDatabase(int amount){

    }
}
