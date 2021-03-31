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
    Button btnRead, btnWrite;
    TextView txtTitle, txtContent, txtAuthor, txtDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend_test);

        initLayoutViews();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.document("news/firstNew");
        String content = News.createSampleContent(4, 5);
        News paper = new News("test", content, "uncategorized", "admin");

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTitle.setText(paper.getTitle());
                txtContent.setText(paper.getContent());
                txtAuthor.setText(paper.getAuthorUsername());
                txtDate.setText(paper.getWriteDate().toString());
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseSample databaseSample = new DatabaseSample();
                databaseSample.writeToNewsDatabase(3);
            }
        });
    }

    public void initLayoutViews(){
        btnRead = (Button) findViewById(R.id.btnRead);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        txtTitle = (TextView) findViewById(R.id.sample_title);
        txtContent = (TextView) findViewById(R.id.sample_content);
        txtContent.setMovementMethod(new ScrollingMovementMethod());
        txtAuthor = (TextView) findViewById(R.id.sample_author);
        txtDate = (TextView) findViewById(R.id.sample_date);
    }
}