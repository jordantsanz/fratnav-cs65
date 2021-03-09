package com.example.fratnav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fratnav.R;
import com.example.fratnav.databaseHelpers.AuthenticationHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.forum.PostActivity;
import com.example.fratnav.models.Post;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RVFeedAdapter extends RecyclerView.Adapter<RVFeedAdapter.MyView> {
    private ArrayList<Post> posts;
    String currentUserId;
    FirebaseUser currentUser;


    public class MyView extends RecyclerView.ViewHolder {
        TextView postUser;
        TextView postText;
        TextView postLikes;
        TextView tag1;
        TextView tag2;
        TextView tag3;
        String userDisplay;
        Context context;

        public MyView(View view) {
            super(view);

            postUser = (TextView) view.findViewById(R.id.postUser);
            postText = (TextView) view.findViewById(R.id.postText);
            postLikes = (TextView) view.findViewById(R.id.likes);
            tag1 = (TextView) view.findViewById(R.id.tag1);
            tag2 = (TextView) view.findViewById(R.id.tag2);
            tag3 = (TextView) view.findViewById(R.id.tag3);

            context = view.getContext();

            // Populate the data into the template view using the data object

        }
    }

    public RVFeedAdapter(Context context, ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        Post post = getItem(position);
        currentUser = AuthenticationHelper.getCurrentUser();
        currentUserId = currentUser.getUid();
        String userDisplay = "@" + post.username;
        holder.setIsRecyclable(false);


        holder.postUser.setText(userDisplay);
        holder.postText.setText(post.text);
        if (post.attributes != null) {
            switch (post.attributes.size()) {
                case 0:
                    holder.tag1.setText("");
                    holder.tag2.setText("");
                    holder.tag3.setText("");
                    break;
                case 1:
                    holder.tag1.setText(post.attributes.get(0));
                    holder.tag2.setText("");
                    holder.tag3.setText("");
                    break;
                case 2:
                    holder.tag1.setText(post.attributes.get(0));
                    holder.tag2.setText(post.attributes.get(1));
                    holder.tag3.setText("");
                    break;
                case 3:
                    holder.tag1.setText(post.attributes.get(0));
                    holder.tag2.setText(post.attributes.get(1));
                    holder.tag3.setText(post.attributes.get(2));
                    break;
                default:
                    break;
            }

            setBackgrounds(holder.tag1, holder.tag2, holder.tag3);
        }


    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public Post getItem(int position) {
        return posts.get(position);
    }


    @SuppressLint("ResourceAsColor")
    public void setBackgrounds(TextView tag1, TextView tag2, TextView tag3) {
        ArrayList<TextView> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);

        for (TextView tag : tags) {
            String text = tag.getText().toString();
            Log.d("theText", text);
            switch (text) {
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
                case "ΔΣΘ":
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
                case "ΑΦΑ":
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