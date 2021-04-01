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

import com.backend.DatabaseManagement;
import com.backend.DatabaseSample;
import com.backend.News;
import com.backend.NewsPreview;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class BackendTestActivity extends AppCompatActivity {
    Button btnRead, btnWrite;
    TextView txtTitle, txtContent, txtAuthor, txtDate;
    ImageView imgThumbnail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend_test);

        initLayoutViews();

        DatabaseManagement databaseManagement = new DatabaseManagement();
        DatabaseSample databaseSample = new DatabaseSample();

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               databaseManagement.getLatestPreview(10);
//                for (NewsPreview sample : previews){
//                    txtTitle.setText(sample.getTitle());
//                    txtContent.setText(sample.getPreviewContent());
//                    txtAuthor.setText(sample.getAuthorUsername());
//                    txtDate.setText(sample.getCreatedDate().toString());
//                    Picasso.get().load(sample.getThumbnailURL()).into(imgThumbnail);
//                    break;
//                }

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