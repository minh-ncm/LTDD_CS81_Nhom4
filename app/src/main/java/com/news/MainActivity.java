package com.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backend.New;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnOne, btnTwo;
    TextView txtHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOne = (Button) findViewById(R.id.btnOne);
        btnTwo = (Button) findViewById(R.id.btnTwo);
        txtHello = (TextView) findViewById(R.id.textView);

        Map<String, Object> docData = new HashMap<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.document("news/firstNew");

        String content = "";
        try {
            content = New.createSampleContent();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        New paper = new New("test", content);

        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                docRef.set(paper);
                txtHello.setText(paper.getContent());
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        New n = documentSnapshot.toObject(New.class);
                        txtHello.setText(n.getTitle());
                    }
                });
            }
        });
    }
}