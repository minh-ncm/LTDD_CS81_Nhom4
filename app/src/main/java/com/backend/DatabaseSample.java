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
    private  String[] sampleImages = {"https://firebase.google.com/downloads/brand-guidelines/PNG/logo-built_black.png",
            "https://www.zerone-consulting.com/wp-content/uploads/2017/10/Cloud-Firestore-1.png",
            "https://r00t4bl3.com/uploads/android-studio-6464af9314a635bd20494fd1b343d2fa.png",
    };
    private String getRandomType(String[] array){
        int index = new Random().nextInt(array.length);
        return array[index];
    }

    public News generateSampleNews() {
        Lorem lorem = new LoremIpsum();
        News sample = new News();
        sample.setType(getRandomType(News.typeNames));
        sample.setAuthorUsername("admin");
        sample.setTitle(lorem.getWords(3, 5));
        for (int i = 0; i < new Random().nextInt(5) + 1; i++)
            sample.addContent(lorem.getParagraphs(1, 1));
        for (int i = 0; i < new Random().nextInt(5) + 1; i++)
            sample.addImageURL(sampleImages[new Random().nextInt(sampleImages.length)]);
        return sample;
    }

    public void writeToNewsDatabase(int amount){
        for(int i = 1; i <= amount; i++){
            News sample = generateSampleNews();
            DatabaseManagement management = new DatabaseManagement();
            management.writeNewsToDatabase(sample);
        }
    }

    public void writeToUsersDatabase(int amount){

    }
}
