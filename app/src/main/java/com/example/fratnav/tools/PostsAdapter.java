package com.example.fratnav.tools;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;

import com.example.fratnav.R;
import com.example.fratnav.callbacks.likePostCallback;
import com.example.fratnav.databaseHelpers.AuthenticationHelper;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.models.Post;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PostsAdapter extends ArrayAdapter<Post> {
    Post post;
    boolean userDidLike = false;
    FirebaseUser currentUser;
    String currentUserId;
    public PostsAdapter(Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        post = getItem(position);

        currentUser = AuthenticationHelper.getCurrentUser();
        currentUserId = currentUser.getUid();
        Log.d("post", post.id);


        if (post.usersLiked != null){
            Log.d("post", post.usersLiked.values().toString());
            for (String userId : post.usersLiked.values()) {
                if (userId.equals(currentUserId)) {
                    userDidLike = true;
                    break;
                }
            }
        }








        // Lookup view for data population
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post, parent, false);
        }
        TextView postUser = (TextView) convertView.findViewById(R.id.postUser);
        TextView postText = (TextView) convertView.findViewById(R.id.postText);
        TextView postLikes = (TextView) convertView.findViewById(R.id.likes);

        TextView tag1 = (TextView) convertView.findViewById(R.id.tag1);
        TextView tag2 = (TextView) convertView.findViewById(R.id.tag2);
        TextView tag3 = (TextView) convertView.findViewById(R.id.tag3);

        if (post.attributes != null) {
            switch (post.attributes.size()) {
                case 0:
                    tag1.setText("");
                    tag2.setText("");
                    tag3.setText("");
                    break;
                case 1:
                    tag1.setText(post.attributes.get(0));
                    tag2.setText("");
                    tag3.setText("");
                    break;
                case 2:
                    tag1.setText(post.attributes.get(0));
                    tag2.setText(post.attributes.get(1));
                    tag3.setText("");
                    break;
                case 3:
                    tag1.setText(post.attributes.get(0));
                    tag2.setText(post.attributes.get(1));
                    tag3.setText(post.attributes.get(2));
                    break;
                default:
                    break;
            }

            setBackgrounds(tag1, tag2, tag3);
        }


        ImageButton heart = convertView.findViewById(R.id.heart);
        heart.setTag(position);

        // Populate the data into the template view using the data object
        String userDisplay = "@" + post.username;
        postUser.setText(userDisplay);
        postText.setText(post.text);
        String likes = post.likes +"";
        postLikes.setText(likes);


//        if (post.likes == 0){
//            postLikes.setText(post.likes);
//        }
        // Return the completed view to render on screen
        return convertView;
    }

//    public void likes( View view){
//        ImageView
//    }

    @Nullable
    @Override
    public Post getItem(int position) {
        return super.getItem(position);
    }

    public void setBackgrounds(TextView tag1, TextView tag2, TextView tag3){
        ArrayList<TextView> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);

        for (TextView tag : tags){
            String text = tag.getText().toString();
            Log.d("theText", text);
            switch (text){
                case "Alpha Chi":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_teal);
                    break;
                case "APhi":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_purple);
                    break;
                default:
                    tag.setBackgroundColor(Color.GREEN);
            }

        }
    }
}
