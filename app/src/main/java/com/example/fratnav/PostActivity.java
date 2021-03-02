package com.example.fratnav;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fratnav.callbacks.getCommentsByPostIdCallback;
import com.example.fratnav.callbacks.getPostByIdCallback;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.models.Comment;
import com.example.fratnav.models.Post;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    Post thePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        TextView username = (TextView)findViewById(R.id.postActivityUser);
        TextView textView = (TextView) findViewById(R.id.postActivityText);


        PostDatabaseHelper.getPostById(getIntent().getStringExtra(Forum.POST_ID_KEY), new getPostByIdCallback() {
            @Override
            public void onCallback(Post post) {
                thePost = post;
                textView.setText(post.text);
                username.setText(post.username);

                PostDatabaseHelper.getAllCommentsByPostId(post, new getCommentsByPostIdCallback() {
                    @Override
                    public void onCallback(ArrayList<Comment> comments) {
                        Log.d("comments", comments.toString());
                    }
                });
            }
        });
    }

    public void createNewComment(View view){
        EditText ed = findViewById(R.id.commentEditText);
        Comment comment = new Comment(getIntent().getStringExtra(Forum.USER_ID_KEY), ed.getText().toString());

        PostDatabaseHelper.addPostComment(thePost, comment);
    }
}