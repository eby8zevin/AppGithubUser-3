package com.ahmadabuhasan.appgithubuser.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmadabuhasan.appgithubuser.adapter.FollowAdapter;
import com.ahmadabuhasan.appgithubuser.databinding.FollowersFragmentBinding;
import com.ahmadabuhasan.appgithubuser.viewmodel.UserViewModel;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;

public class FollowersFragment extends Fragment {

    FollowersFragmentBinding binding;
    private UserViewModel followersViewModel;
    private FollowAdapter followAdapter;

    public FollowersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FollowersFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        followAdapter = new FollowAdapter(getContext());
        followAdapter.notifyDataSetChanged();
        binding.rvFollowers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvFollowers.setAdapter(followAdapter);
        binding.rvFollowers.setHasFixedSize(true);

        followersViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);
        followersViewModel.setFollowers(UserDetailActivity.dataUser);
        followersViewModel.getFollowersUser().observe(getViewLifecycleOwner(), follows -> {
            if (follows.size() != 0) {
                followAdapter.setFollowList(follows);
            } else {
                Toasty.info(requireContext(), "Followers Not Found", Toasty.LENGTH_SHORT).show();
            }
        });
        followersViewModel.isLoading().observe(getViewLifecycleOwner(), this::showLoading);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        followersViewModel.setFollowers(UserDetailActivity.dataUser);
    }

    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}