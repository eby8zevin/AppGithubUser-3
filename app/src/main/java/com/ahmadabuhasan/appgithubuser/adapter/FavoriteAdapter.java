package com.ahmadabuhasan.appgithubuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.appgithubuser.databinding.ItemRowUserBinding;
import com.ahmadabuhasan.appgithubuser.model.UserDetail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final ArrayList<UserDetail> userDetailArrayList = new ArrayList<>();
    public static Context context;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public FavoriteAdapter(Context context) {
        FavoriteAdapter.context = context;
    }

    public void setFavorite(ArrayList<UserDetail> data) {
        userDetailArrayList.clear();
        userDetailArrayList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRowUserBinding binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        UserDetail user = userDetailArrayList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatarUrl())
                .apply(new RequestOptions().override(350, 350))
                .into(holder.binding.imgAvatar);
        holder.binding.tvHtml.setText(user.getHtmlUrl());
        holder.binding.tvUsername.setText(String.format("@%s", user.getUsername()));

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(userDetailArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return userDetailArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemRowUserBinding binding;

        public ViewHolder(ItemRowUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(UserDetail data);
    }
}