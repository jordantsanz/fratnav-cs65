package com.example.fratnav.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fratnav.R;
import com.example.fratnav.models.Comment;
import com.example.fratnav.models.Post;

import java.util.ArrayList;

public class CommentsAdapter extends ArrayAdapter<Comment> {

    public CommentsAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Comment comment = getItem(position);
        // Lookup view for data population
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment, parent, false);
        }

//        ListView lv = (ListView) convertView.findViewById(android.R.id.list);
//        Typeface tf = Typeface.createFromAsset(getAssets(),)
        TextView commentUser = (TextView) convertView.findViewById(R.id.commentUser);
        TextView commentText = (TextView) convertView.findViewById(R.id.commentText);
        // Populate the data into the template view using the data object



        String user = "@" + comment.usernamePoster;
        commentUser.setText(user);
        commentText.setText(comment.comment);

        // Return the completed view to render on screen
        return convertView;
    }
}