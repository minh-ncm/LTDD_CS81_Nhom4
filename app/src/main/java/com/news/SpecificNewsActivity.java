package com.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.backend.DatabaseManagement;
import com.backend.NewsPreview;

import java.util.List;

public class SpecificNewsActivity extends AppCompatActivity {
    private final DatabaseManagement databaseManagement = new DatabaseManagement();

    private TextView tvContents, tvType, tvTitle, tvCreatedDate, tvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_news);

        init();
        String username = getIntent().getStringExtra("authorUsername");
        String title = getIntent().getStringExtra("title");
        loadNewsPreview(username, title);
        loadContents(username, title);
    }

    void init() {
        tvType = findViewById(R.id.full_news_type);
        tvTitle = findViewById(R.id.full_news_title);
        tvContents = findViewById(R.id.full_news_contents);
        tvCreatedDate = findViewById(R.id.full_news_created_date);
        tvAuthor = findViewById(R.id.full_news_author);
    }

    void loadContents(String author, String title) {
        databaseManagement.getNewsContents(new DatabaseManagement.newsContentsCallback() {
            @Override
            public void onCallback(List<String> contents) {
                StringBuilder builder = new StringBuilder();
                for(String s : contents) {
                    builder.append(s).append("\n");
                }
                tvContents.setText(builder.toString());
            }
        }, author, title);
    }
    void loadNewsPreview(String author, String title) {
        databaseManagement.getSpecificNewsPreview(new DatabaseManagement.newsPreviewCallback() {
            @Override
            public void onCallback(NewsPreview news) {
                tvTitle.setText(news.getTitle());
                tvType.setText(news.getType());
                tvCreatedDate.setText(news.getCreatedDate().toString());
                tvAuthor.setText(news.getAuthorUsername());
            }
        }, author, title);
    }
}