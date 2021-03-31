        package com.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.backend.DatabaseSample;
import com.backend.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BackendTestActivity extends AppCompatActivity {
    Button btnOne, btnTwo;
    TextView txtHello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend_test);

        btnOne = (Button) findViewById(R.id.btnOne);
        btnTwo = (Button) findViewById(R.id.btnTwo);
        txtHello = (TextView) findViewById(R.id.textView);
        txtHello.setMovementMethod(new ScrollingMovementMethod());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.document("news/firstNew");
        String content = News.createSampleContent(4, 5);
        News paper = new News("test", content, "");

        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                docRef.set(paper);
                txtHello.setText(paper.getPreviewContent(200));
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseSample databaseSample = new DatabaseSample();
                databaseSample.writeToNewsDatabase(3);
            }
        });
    }
}