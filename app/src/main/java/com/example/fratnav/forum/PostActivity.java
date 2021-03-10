package com.example.fratnav.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.fratnav.databaseHelpers.AuthenticationHelper;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.models.Comment;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.example.fratnav.tools.CommentsAdapter;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    Post thePost;
    CommentsAdapter adapter;
    ArrayList<Comment> arrayOfComments;
    User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //connects XML
        setContentView(R.layout.activity_post);

        //gets the current user
        FirebaseUser currentUser = AuthenticationHelper.getCurrentUser();

        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                // sets the user
                userInfo = user;
            }
        });

        //gets all of the textviews that need informatoin to be rendered
        TextView username = (TextView)findViewById(R.id.postActivityUser);
        TextView textView = (TextView) findViewById(R.id.postActivityText);
        TextView userSexuality = (TextView) findViewById(R.id.postActivityUserSexuality);
        TextView userGender= (TextView) findViewById(R.id.postActivityUserGender);
        TextView userAffilation= (TextView) findViewById(R.id.postActivityUserAffiliation);
        TextView userYear= (TextView) findViewById(R.id.postActivityUserYear);

        //sets a new arraylist and adapter
        arrayOfComments = new ArrayList<>();
        adapter = new CommentsAdapter(getApplicationContext(), arrayOfComments);

        //sets the user information, and post infromation on the XML
        PostDatabaseHelper.getPostById(getIntent().getStringExtra(Forum.POST_ID_KEY), new getPostByIdCallback() {
            @Override
            public void onCallback(Post post) {
                thePost = post;
                textView.setText(post.text);
                String usernameString = "@" + post.username;
                username.setText(usernameString);
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
                        if (user.gender ==  null ){
                            userGender.setBackgroundResource(R.drawable.background_color_background);
                            userGender.setTextColor(getResources().getColor(R.color.backgroundColor));
                        }
                        else {
                            userGender.setText(user.gender);
                        }
                        if (user.sexuality == null ){
                            userSexuality.setTextColor(getResources().getColor(R.color.backgroundColor));
                            userSexuality.setBackgroundResource(R.drawable.background_color_background);
                        }
                        else{
                            userSexuality.setText(user.sexuality);
                        }
                        if(user.year == null){
                            userYear.setBackgroundResource(R.drawable.background_color_background);
                            userYear.setTextColor(getResources().getColor(R.color.backgroundColor));
                        }
                        else{
                            userYear.setText(user.year);
                        }

                    }
                });
                //adds the comments on the post to the adapter
                PostDatabaseHelper.getAllCommentsByPostId(post, new getCommentsByPostIdCallback() {
                    @Override
                    public void onCallback(ArrayList<Comment> comments) {
                        Log.d("comments", comments.toString());
                        for (Comment comment : comments){
                            adapter.add(comment);
                        }
                        //adapter is updated
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        //comments adapter
        ListView list = findViewById(android.R.id.list);
        list.setAdapter(adapter); // sets adapter for list
    }

    //cancles the comment and returns to the forum
    public void cancelComment(View view) {
        Intent intent = new Intent(PostActivity.this, Forum.class);
        startActivity(intent);
        finish();

    }
    //creates the comment and send sthe comment to the database
    public void createNewComment(View view){
        EditText ed = findViewById(R.id.commentEditText);
        String string = ed.getText().toString();
        if (string.equals("")){ Toast.makeText(PostActivity.this, "Please enter in a comment to post.", Toast.LENGTH_SHORT).show();
        return; }
        Comment comment = new Comment(getIntent().getStringExtra(Forum.USER_ID_KEY), ed.getText().toString(), userInfo.username);

        arrayOfComments.add(comment);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        adapter.notifyDataSetChanged();
        PostDatabaseHelper.addPostComment(thePost, comment);

        Toast.makeText(this, "Comment left.", Toast.LENGTH_SHORT).show();
    }
}