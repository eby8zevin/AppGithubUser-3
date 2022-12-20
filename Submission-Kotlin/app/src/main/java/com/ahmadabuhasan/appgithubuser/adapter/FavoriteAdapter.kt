package com.ahmadabuhasan.appgithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmadabuhasan.appgithubuser.databinding.ItemsUserBinding
import com.ahmadabuhasan.appgithubuser.model.ResponseDetailUser
import com.bumptech.glide.Glide

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    private val listFavorite = ArrayList<ResponseDetailUser>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: ArrayList<ResponseDetailUser>) {
        val diffCallback = DiffUtilCallback(listFavorite, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listFavorite.clear()
        listFavorite.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemsUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val fav = listFavorite[position]
        holder.bind(fav)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(
                listFavorite[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    class ListViewHolder(private val _binding: ItemsUserBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun bind(fav: ResponseDetailUser) {
            _binding.tvAccount.text = fav.htmlUrl
            _binding.tvUsername.text = fav.login

            Glide.with(itemView.context)
                .load(fav.avatarUrl)
                .skipMemoryCache(true)
                .into(_binding.imgAvatar)
        }
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(selected: ResponseDetailUser)
    }

    class DiffUtilCallback(
        private val oldList: List<ResponseDetailUser>,
        private val newList: List<ResponseDetailUser>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.hashCode() == newItem.hashCode()
        }

        @Override
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}