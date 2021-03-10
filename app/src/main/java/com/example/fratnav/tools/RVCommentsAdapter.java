package com.example.fratnav.tools;

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
import com.example.fratnav.models.Comment;
import com.example.fratnav.models.Post;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RVCommentsAdapter extends RecyclerView.Adapter<RVCommentsAdapter.MyView> {
    private ArrayList<Comment> comments;
    String currentUserId;
    FirebaseUser currentUser;


    public class MyView extends RecyclerView.ViewHolder {
        TextView commentUser;
        TextView commentText;
        String userDisplay;
        Context context;

        public MyView(View view) {
            super(view);

            commentUser = (TextView) view.findViewById(R.id.commentUser);
            commentText = (TextView) view.findViewById(R.id.commentText);

            context = view.getContext();

            // Populate the data into the template view using the data object

        }
    }

    public RVCommentsAdapter(Context context, ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        Comment comment = getItem(position);
        currentUser = AuthenticationHelper.getCurrentUser();
        currentUserId = currentUser.getUid();
        String userDisplay = "@" + comment.usernamePoster;
        holder.setIsRecyclable(false);


        holder.commentUser.setText(userDisplay);
        holder.commentText.setText(comment.comment);


    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

    public Comment getItem(int position) {
        return comments.get(position);
    }


}
