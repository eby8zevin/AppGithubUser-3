package com.ahmadabuhasan.appgithubuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.appgithubuser.databinding.ItemRowUserBinding;
import com.ahmadabuhasan.appgithubuser.model.SearchData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final ArrayList<SearchData> searchData = new ArrayList<>();
    public static Context context;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public SearchAdapter(Context context) {
        SearchAdapter.context = context;
    }

    public void setSearchData(ArrayList<SearchData> data) {
        searchData.clear();
        searchData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRowUserBinding binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchData user = searchData.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatarUrl())
                .apply(new RequestOptions().override(350, 350))
                .into(holder.binding.imgAvatar);
        holder.binding.tvHtml.setText(user.getHtmlUrl());
        holder.binding.tvUsername.setText(String.format("@%s", user.getUsername()));

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(searchData.get(position)));
    }

    @Override
    public int getItemCount() {
        return searchData.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        ItemRowUserBinding binding;

        public SearchViewHolder(@NonNull ItemRowUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(SearchData data);
    }
}