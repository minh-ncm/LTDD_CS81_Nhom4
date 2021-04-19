package com.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backend.DatabaseManagement;
import com.backend.NewsPreview;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FullNewsActivity extends AppCompatActivity {
    private final DatabaseManagement databaseManagement = new DatabaseManagement();

    private TextView tvType, tvTitle, tvCreatedDate, tvAuthor;
    private ImageView ivThumbnail;
    private LinearLayout llContents;

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
        tvCreatedDate = findViewById(R.id.full_news_created_date);
        tvAuthor = findViewById(R.id.full_news_author);
        ivThumbnail = findViewById(R.id.full_news_thumbnail);
        llContents = findViewById(R.id.full_news_contents);
    }

    TextView applyParagraphTheme() {
        TextView textView = new TextView(FullNewsActivity.this);
        textView.setTextColor(getColor(R.color.eerie_black));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_regular_contents));
        textView.setLineSpacing(getResources().getDimension(R.dimen.linespace_paragraph), 1);
        textView.setTypeface(ResourcesCompat.getFont(FullNewsActivity.this, R.font.open_sans));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0,
                (int) getResources().getDimension(R.dimen.margin_bottom_paragraph));
        textView.setLayoutParams(layoutParams);
        return  textView;
    }
    void loadContents(String author, String title) {
        databaseManagement.getNewsContents(new DatabaseManagement.newsContentsCallback() {
            @Override
            public void onCallback(List<String> contents) {
                StringBuilder builder = new StringBuilder();
                for(int i = 0; i < contents.size(); i++) {
                    builder.append(contents.get(i));
                    if(i < contents.size() - 1)
                        builder.append("\n");
                }

                String[] paragraphs = builder.toString().split("\n");
                for(String s : paragraphs) {
                    TextView textView = applyParagraphTheme();
                    textView.setText("\t\t\t" + s);
                    llContents.addView(textView);
                }
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
                Picasso.get().load(news.getThumbnailURL())
                        .resize(ivThumbnail.getWidth(), 0)
                        .into(ivThumbnail);
            }
        }, author, title);
    }

    public void onClickBack(View view) {
        startActivity(new Intent(FullNewsActivity.this, BackendTestActivity.class));
    }
}