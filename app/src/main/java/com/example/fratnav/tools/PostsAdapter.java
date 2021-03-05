package com.example.fratnav.tools;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fratnav.R;
import com.example.fratnav.models.Post;

import java.util.ArrayList;

public class PostsAdapter extends ArrayAdapter<Post> {
    public PostsAdapter(Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Post post = getItem(position);
        // Lookup view for data population
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post, parent, false);
        }
        TextView postUser = (TextView) convertView.findViewById(R.id.postUser);
        TextView postText = (TextView) convertView.findViewById(R.id.postText);
        TextView postLikes = (TextView) convertView.findViewById(R.id.likes);
        ImageView heart = (ImageView) convertView.findViewById(R.id.heart);

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //heart.setBackground();  change
                //update likes in the database
            }
        });
        // Populate the data into the template view using the data object
        String userDisplay = "@" + post.username;
        postUser.setText(userDisplay);
        postText.setText(post.text);
        Log.d("likessssssss", post.likes+"");
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
}
