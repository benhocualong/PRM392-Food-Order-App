package com.harusora.longcn.apporderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harusora.longcn.apporderfood.R;
import com.harusora.longcn.apporderfood.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(Context context) {
        this.context = context;
        this.commentList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void addComments(List<Comment> comments) {
        commentList.addAll(comments);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView tvUsername, tvDetail, tvDate;

        public ViewHolder(View view) {
            super(view);
            tvUsername = view.findViewById(R.id.tv_username);
            tvDetail = view.findViewById(R.id.tv_commentContent);
            tvDate = view.findViewById(R.id.tv_commentDate);
        }

        public void bind(Comment comment) {
            tvUsername.setText("username: " + comment.getUsername());
            tvDetail.setText("Nội dung: " + comment.getDetail());
            tvDate.setText("Ngày bình luận: " + comment.getDate());
        }
    }
}
