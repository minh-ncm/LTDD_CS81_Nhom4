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

    public void writeToNewsDatabase(int amount){
        for(int i = 1; i <= amount; i++){
            News sample = generateSampleNews();
            sample.writeNewsToDatabase();
        }
    }

    public void writeToUsersDatabase(int amount){

    }
}
