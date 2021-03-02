package com.example.fratnav.callbacks;

import com.example.fratnav.models.Comment;

import java.util.ArrayList;

public interface getCommentsByPostIdCallback {
    void onCallback(ArrayList<Comment> comments);
}
