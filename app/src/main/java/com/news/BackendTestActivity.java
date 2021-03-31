        package com.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backend.DatabaseSample;
import com.backend.News;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


        public class BackendTestActivity extends AppCompatActivity {
    Button btnRead, btnWrite;
    TextView txtTitle, txtContent, txtAuthor, txtDate;
    ImageView imgThumbnail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend_test);

        initLayoutViews();

        DatabaseSample databaseSample = new DatabaseSample();

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News sample = databaseSample.generateSampleNews();
                txtTitle.setText(sample.getTitle());
                txtContent.setText(sample.getContent());
                txtAuthor.setText(sample.getAuthorUsername());
                txtDate.setText(sample.getWriteDate().toString());
                Picasso.get().load(sample.getThumbnailURL()).into(imgThumbnail);
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseSample.writeToNewsDatabase(1);
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
        imgThumbnail = (ImageView) findViewById(R.id.backend_thumbnail);
    }
}