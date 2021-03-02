package com.example.fratnav;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fratnav.callbacks.getCommentsByPostIdCallback;
import com.example.fratnav.callbacks.getPostByIdCallback;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.models.Comment;
import com.example.fratnav.models.Post;
import com.example.fratnav.tools.CommentsAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    Post thePost;
    CommentsAdapter adapter;
    ArrayList<Comment> arrayOfComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        TextView username = (TextView)findViewById(R.id.postActivityUser);
        TextView textView = (TextView) findViewById(R.id.postActivityText);

        arrayOfComments = new ArrayList<>();
        adapter = new CommentsAdapter(getApplicationContext(), arrayOfComments);


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
                        for (Comment comment : comments){
                            adapter.add(comment);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        ListView list = findViewById(android.R.id.list);
        list.setAdapter(adapter); // sets adapter for list
    }

    public void createNewComment(View view){
        EditText ed = findViewById(R.id.commentEditText);
        Comment comment = new Comment(getIntent().getStringExtra(Forum.USER_ID_KEY), ed.getText().toString());

        arrayOfComments.add(comment);

        adapter.notifyDataSetChanged();
        PostDatabaseHelper.addPostComment(thePost, comment);
    }
}