package com.example.fratnav.tools;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
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
import java.util.Iterator;

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

        if (post.usersLiked != null){
            Log.d("post", post.usersLiked.values().toString());
            for (String userId : post.usersLiked.values()) {
                if (userId.equals(currentUserId)) {
                    userDidLike = true;
                    heart.setBackgroundResource(R.drawable.filledlike);
//                    break;
                }
                else{
                    heart.setBackgroundResource(R.drawable.like);
                }
            }
        }

        heart.setTag(position);

        // Populate the data into the template view using the data object
        String userDisplay = "@" + post.username;
        postUser.setText(userDisplay);
        postText.setText(post.text);
        String likes = post.likes +"";
        postLikes.setText(likes);


        // Return the completed view to render on screen
        return convertView;
    }


    @Nullable
    @Override
    public Post getItem(int position) {
        return super.getItem(position);
    }


    @SuppressLint("ResourceAsColor")
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
                case "Sig Nu":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "TDX":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "Zete":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "Sigma Delt":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_purple);
                    break;
                case "Tabard":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_teal);
                    break;
                case "Tri-Kap":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "Kappa":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_purple);
                    break;
                case "Hereot":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "Phi Delt":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "KD":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_purple);
                    break;
                case "KDE":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_purple);
                    break;
                case "Phi Tau":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_teal);
                    break;
                case "GDX":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "Psi U":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "Chi Delt":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_purple);
                    break;
                case "Chi Gam":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "EKT":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_purple);
                    break;
                case "Deltas":
                    tag.setBackgroundResource(R.drawable.blue_background);
                    break;
                case "AXiD":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_purple);
                    break;
                case "Beta":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "BG":
                    tag.setBackgroundResource(R.drawable.frat_tag_filled_background);
                    break;
                case "Alpha Theta":
                    tag.setBackgroundResource(R.drawable.rounded_corner_view_teal);
                    break;
                case "Alphas":
                    tag.setBackgroundResource(R.drawable.blue_background);
                    break;
                case "AKA":
                    tag.setBackgroundResource(R.drawable.blue_background);
                    break;
                case "POC":
                    tag.setBackgroundResource(R.drawable.grey_background);
                    break;
                case "LGBTQ":
                    tag.setBackgroundResource(R.drawable.grey_background);
                    break;
                case "First-Gen":
                    tag.setBackgroundResource(R.drawable.grey_background);
                    break;
                case "Low Income":
                    tag.setBackgroundResource(R.drawable.grey_background);
                    break;
                default:
                    tag.setBackgroundResource(R.drawable.post_background);
            }
        }
    }
}
