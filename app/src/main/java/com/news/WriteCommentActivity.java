package com.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.backend.Comment;
import com.backend.DatabaseManagement;

public class WriteCommentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_comment);
    }

    public void onClickWriteCommentToDatabase(View view) {
        TextView tvComment = findViewById(R.id.commentContent);
        String authorUsername = getIntent().getStringExtra("authorUsername");
        String title = getIntent().getStringExtra("title");
        // TODO: get logged in username
        String username = "admin";
        String text = tvComment.getText().toString().trim();
        
        if(!text.equals("")) {
            Comment comment = new Comment(username, text, authorUsername, title);
            DatabaseManagement databaseManagement = new DatabaseManagement();

            databaseManagement.writeCommentToDatabase(comment);

            // Go back to article after post comment
            Intent intent = new Intent(WriteCommentActivity.this, FullNewsActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("authorUsername", authorUsername);
            startActivity(intent);
        }
    }
}
