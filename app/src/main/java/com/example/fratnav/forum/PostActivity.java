package com.example.fratnav.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fratnav.R;
import com.example.fratnav.callbacks.getCommentsByPostIdCallback;
import com.example.fratnav.callbacks.getPostByIdCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.models.Comment;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.example.fratnav.tools.CommentsAdapter;

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
        TextView userSexuality = (TextView) findViewById(R.id.postActivityUserSexuality);
        TextView userGender= (TextView) findViewById(R.id.postActivityUserGender);
        TextView userAffilation= (TextView) findViewById(R.id.postActivityUserAffiliation);
        TextView userYear= (TextView) findViewById(R.id.postActivityUserYear);


        arrayOfComments = new ArrayList<>();
        adapter = new CommentsAdapter(getApplicationContext(), arrayOfComments);


        PostDatabaseHelper.getPostById(getIntent().getStringExtra(Forum.POST_ID_KEY), new getPostByIdCallback() {
            @Override
            public void onCallback(Post post) {
                thePost = post;
                textView.setText(post.text);
                username.setText(post.username);
                UserDatabaseHelper.getUserById(post.userID, new getUserByIdCallback() {
                    @Override
                    public void onCallback(User user) {
                        if (user.houseAffiliation){
                            String affiliated = "Affiliated";
                            userAffilation.setText(affiliated);
                        }
                        else{
                            String notAffiliated = "Not Affiliated";
                            userAffilation.setText(notAffiliated);
                        }

                        userGender.setText(user.gender);
                        userSexuality.setText(user.sexuality);
                        userYear.setText(user.year);

                    }
                });

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

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        adapter.notifyDataSetChanged();
        PostDatabaseHelper.addPostComment(thePost, comment);

        Toast.makeText(this, "Comment left.", Toast.LENGTH_SHORT).show();
    }
}