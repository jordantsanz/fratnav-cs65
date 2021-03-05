package com.example.fratnav.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fratnav.R;
import com.example.fratnav.models.Post;

import java.util.ArrayList;

public class RVPostsAdapter extends RecyclerView.Adapter<RVPostsAdapter.MyView>{
    private ArrayList<Post> posts;


    public class MyView extends RecyclerView.ViewHolder{
        TextView postUser;
        TextView postText;
        String userDisplay;

        public MyView(View view) {
            super(view);

            postUser = (TextView) view.findViewById(R.id.postUser);
            postText = (TextView) view.findViewById(R.id.postText);
            // Populate the data into the template view using the data object
        }
    }

    public RVPostsAdapter(Context context, ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post,parent,false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        Post post = getItem(position);
        String userDisplay = "@" + post.username;

        holder.postUser.setText(userDisplay);
        holder.postText.setText(post.text);

    }


    @Override
    public int getItemCount(){
        return posts.size();
    }

    public Post getItem(int position) {
        return posts.get(position);
    }
}
