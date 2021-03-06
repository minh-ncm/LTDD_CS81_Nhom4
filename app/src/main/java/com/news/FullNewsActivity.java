package com.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.backend.Comment;
import com.backend.DatabaseManagement;
import com.backend.NewsPreview;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.TypeVariable;
import java.util.List;

public class FullNewsActivity extends AppCompatActivity {
    private final DatabaseManagement databaseManagement = new DatabaseManagement();

    private TextView tvType, tvTitle, tvCreatedDate, tvAuthor;
    private ImageView ivThumbnail;
    private LinearLayout contentsSection, recommendsSection, commentsSection;

    private String authorUsername, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_news);

        init();
        authorUsername = getIntent().getStringExtra("authorUsername");
        title = getIntent().getStringExtra("title");
        loadNewsPreview(authorUsername, title);
        loadContents(authorUsername, title);
        loadComments(authorUsername, title);
    }

    void init() {
        tvType = findViewById(R.id.full_news_type);
        tvTitle = findViewById(R.id.full_news_title);
        tvCreatedDate = findViewById(R.id.full_news_created_date);
        tvAuthor = findViewById(R.id.full_news_author);
        ivThumbnail = findViewById(R.id.full_news_thumbnail);
        contentsSection = findViewById(R.id.full_news_contents);
        recommendsSection = findViewById(R.id.full_news_recommend);
        commentsSection = findViewById(R.id.full_news_comments);;
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
                    contentsSection.addView(textView);
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
                loadRecommendNewsPreview(news.getType(), 5);
            }
        }, author, title);
    }
    public void loadRecommendNewsPreview(String type, int amount) {
        databaseManagement.getPreviewsByType(new DatabaseManagement.newsListPreviewsCallback() {
            @Override
            public void onCallback(List<NewsPreview> list) {
                for (NewsPreview preview : list) {
                    if (!preview.getTitle().contentEquals(tvTitle.getText())) {
                        LayoutInflater inflater = LayoutInflater.from(FullNewsActivity.this);
                        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.news_previews, recommendsSection, false);

                        TextView previewTitle = (TextView) layout.getChildAt(1);
                        previewTitle.setText(preview.getTitle());

                        TextView previewsContents = (TextView) layout.getChildAt(2);
                        String text = preview.getPreviewContent();
                        int maxLength = 300;
                        if (text.length() > maxLength)
                            text = text.substring(0, maxLength).concat("...");
                        previewsContents.setText("\t\t" + text);

                        // Go to news detail when is clicked
                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FullNewsActivity.this, FullNewsActivity.class);
                                intent.putExtra("title", preview.getTitle());
                                intent.putExtra("authorUsername", preview.getAuthorUsername());
                                startActivity(intent);
                            }
                        });
                        recommendsSection.addView(layout);
                    }
                }
            }
        }, type, amount);
    }
    // TODO:
    public void loadComments(String authorUsername, String title) {
        databaseManagement.getCommentsOfNews(new DatabaseManagement.commentsListCallback() {
            @Override
            public void onCallback(List<Comment> comments) {
                for (Comment comment : comments) {
                    LayoutInflater inflater = LayoutInflater.from(FullNewsActivity.this);
                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.comment, commentsSection, false);

                    TextView username = (TextView) layout.getChildAt(1);
                    username.setText(comment.getUsername());
                    TextView content = (TextView) layout.getChildAt(2);
                    content.setText(comment.getComment());

                    LinearLayout vote = (LinearLayout) layout.getChildAt(3);
                    TextView upvote = (TextView) vote.getChildAt(1);
                    upvote.setText(Integer.toString(comment.getUpvote()));
                    TextView downvote = (TextView) vote.getChildAt(3);
                    downvote.setText(Integer.toString(comment.getDownvote()));

                    commentsSection.addView(layout);
                }
            }
        }, authorUsername, title);
    }

    public void onClickBack(View view) {
        startActivity(new Intent(FullNewsActivity.this, BackendTestActivity.class));
    }
    public void onClickPopupSizeOptions(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.text_size_popup, null);
        View v = findViewById(R.id.text_size_window);

        int width = (int) (displayMetrics.widthPixels * 0.6);
        int height = (int) (displayMetrics.heightPixels * 0.05);

        PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAsDropDown(view);
    }
    public void onClickChangeTextExtraSmall(View view) {
        tvType.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_extra_small_info));
        tvCreatedDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_extra_small_info));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_extra_small_title));
        for (int i = 0; i < contentsSection.getChildCount(); i ++) {
            TextView textView = (TextView) contentsSection.getChildAt(i);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.text_size_extra_small_contents));
        }
        tvAuthor.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_extra_small_author));
    }
    public void onClickChangeTextSmall(View view) {
        tvType.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_small_info));
        tvCreatedDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_small_info));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_small_title));
        for (int i = 0; i < contentsSection.getChildCount(); i ++) {
            TextView textView = (TextView) contentsSection.getChildAt(i);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.text_size_small_contents));
        }
        tvAuthor.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_small_author));
    }
    public void onClickChangeTextRegular(View view) {
        tvType.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_regular_info));
        tvCreatedDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_regular_info));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_regular_title));
        for (int i = 0; i < contentsSection.getChildCount(); i ++) {
            TextView textView = (TextView) contentsSection.getChildAt(i);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.text_size_regular_contents));
        }
        tvAuthor.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_regular_author));
    }
    public void onClickChangeTextLarge(View view) {
        tvType.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_large_info));
        tvCreatedDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_large_info));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_large_title));
        for (int i = 0; i < contentsSection.getChildCount(); i ++) {
            TextView textView = (TextView) contentsSection.getChildAt(i);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.text_size_large_contents));
        }
        tvAuthor.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_large_author));
    }
    public void onClickChangeTextExtraLarge(View view) {
        tvType.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_extra_large_info));
        tvCreatedDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_extra_large_info));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_extra_large_title));
        for (int i = 0; i < contentsSection.getChildCount(); i ++) {
            TextView textView = (TextView) contentsSection.getChildAt(i);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.text_size_extra_large_contents));
        }
        tvAuthor.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_extra_large_author));
    }
    public void onClickGoToCommentsSection(View view) {
        final int duration = 500;
        ScrollView scrollView = findViewById(R.id.full_news_scrollview);
        ObjectAnimator.ofInt(scrollView, "scrollY",  commentsSection.getTop())
                .setDuration(duration)
                .start();
    }

    public void onClickWriteComment(View view) {
        Intent intent = new Intent(FullNewsActivity.this, WriteCommentActivity.class);
        intent.putExtra("authorUsername", authorUsername);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}