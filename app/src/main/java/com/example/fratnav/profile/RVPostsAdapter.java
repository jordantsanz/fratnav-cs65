package com.example.fratnav.profile;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fratnav.R;
import com.example.fratnav.models.Post;

import java.util.ArrayList;

public class RVPostsAdapter extends RecyclerView.Adapter<RVPostsAdapter.MyView> {
    private ArrayList<Post> posts;


    public class MyView extends RecyclerView.ViewHolder {
        TextView postUser;
        TextView postText;
        TextView postLikes;
        TextView tag1;
        TextView tag2;
        TextView tag3;
        String userDisplay;

        public MyView(View view) {
            super(view);

            postUser = (TextView) view.findViewById(R.id.postUser);
            postText = (TextView) view.findViewById(R.id.postText);
            postLikes = (TextView) view.findViewById(R.id.likes);
            tag1 = (TextView) view.findViewById(R.id.tag1);
            tag2 = (TextView) view.findViewById(R.id.tag2);
            tag3 = (TextView) view.findViewById(R.id.tag3);

            // Populate the data into the template view using the data object
        }
    }

    public RVPostsAdapter(Context context, ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        Post post = getItem(position);
        String userDisplay = "@" + post.username;
        String likes = post.likes + "";
        holder.setIsRecyclable(false);

        holder.postUser.setText(userDisplay);
        holder.postText.setText(post.text);
        holder.postLikes.setText(likes);
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
                default:
                    tag.setBackgroundColor(Color.GREEN);
            }
        }
    }
}