package com.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.backend.DatabaseManagement;
import com.backend.DatabaseSample;
import com.backend.NewsPreview;
import com.backend.User;
import com.squareup.picasso.Picasso;

import java.util.List;


public class BackendTestActivity extends AppCompatActivity {
    Button btnRead, btnWrite;
    TextView txtTitle, txtContent, txtAuthor, txtDate;
    ImageView imgThumbnail;
    DatabaseManagement databaseManagement = new DatabaseManagement();
    DatabaseSample databaseSample = new DatabaseSample();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend_test);
        initLayoutViews();
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

    // Example: User - Database interaction method
    public void onClickWriteUserToDatabase(View view) {
        User user = new User("test", "1234");
        databaseManagement.writeUserToDatabase(user);
    }
    public void oncClickReadUserFromDatabase(View view) {
        String username = "admin";
        String pwd = "lorem ipsum";
        StringBuilder builder = new StringBuilder();
        databaseManagement.getUserData(new DatabaseManagement.userCallback() {
            @Override
            public void onCallback(User user) {
                if(user.getUsername().equals(""))
                    txtTitle.setText("username not found");
                else if (!user.getPassword().equals(pwd))
                    txtTitle.setText("wrong password");
                else {
                    builder.append(user.getUsername()).append("\n")
                            .append(user.getPassword()).append("\n");
                    txtContent.setText(builder.toString());
                }
            }
        }, username);
    }
    public void onClickGenerateNewsInDatabase(View view) {
        databaseSample.writeToNewsDatabase(3);
    }
    public void onClickReadPreviewNewsByType(View view) {
        String type = "educations";
        databaseManagement.getPreviewsByType(new DatabaseManagement.newsListPreviewsCallback() {
            @Override
            public void onCallback(List<NewsPreview> list) {
                if (list.size() > 0) {
                    NewsPreview sample = list.get(0);
                    txtTitle.setText(sample.getTitle());
                    Log.d("_out", "total results: " + list.size());
                    txtContent.setText(sample.getPreviewContent());
                    txtAuthor.setText(sample.getAuthorUsername());
                    txtDate.setText(sample.getCreatedDate().toString());
                    Picasso.get().load(sample.getThumbnailURL()).into(imgThumbnail);
                } else {
                    txtContent.setText(new String("Don't have any results"));
                }
            }
        }, type, 10);
    }
    public void onClickGetContents(View view) {
        String username = "admin";
        String title = "efficiantur fabulas qui";
        databaseManagement.getNewsContents(new DatabaseManagement.newsContentsCallback() {
            @Override
            public void onCallback(List<String> contents) {
                StringBuilder builder = new StringBuilder();
                for(String s : contents) {
                    builder.append(s).append("\n");
                }
                txtContent.setText(builder.toString());
            }
        }, username, title);
    }
    public void onClickDeleteNews(View view) {
        databaseManagement.deleteNews("admin", "habitasse posuere iudicabit numquam viris ancillae tale");
    }
    public void onClickGetFullNews(View view) {
        String username = "admin";
        String title = "delectus reformidans eleifend error patrioque litora iaculis";
        Intent intent = new Intent(this, FullNewsActivity.class);
        intent.putExtra("authorUsername", username);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}