package com.backend;

import com.google.firebase.firestore.FirebaseFirestore;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.util.Random;

public class DatabaseSample {
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private String getRandomType(String[] array){
        int index = new Random().nextInt(array.length);
        return array[index];
    }

    public New generateSampleNew() {
        Lorem lorem = new LoremIpsum();
        New sample = new New();
        sample.setTitle(lorem.getWords(5, 10));
        sample.setContent(lorem.getParagraphs(4, 5));
        sample.setType(getRandomType(New.typeNames));
        return sample;
    }

    private void writeToDatabase(int amount, String path){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        for(int i = 0; i < amount; i++) {
            Object sample = new Object();
            if (path.equals("news"))
                sample = generateSampleNew();

            if (path.equals("users"))
                return;
            database.document(path).set(sample);
        }
    }

    public void witeToNewDatabase(int amount){
        writeToDatabase(amount, "news");
    }

    public void witeToUsersDatabase(int amount){
        writeToDatabase(amount, "users");
    }
}
