package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.databinding.ItemNewsBinding

class NewsAdapter(private val newsItemClicked: NewsItemClicked, val context:Context): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val newsList = ArrayList<Article>()

    class NewsViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.binding.date.text = newsList.get(position).publishedAt
        holder.binding.description.text = newsList.get(position).description
        holder.binding.title.text = newsList.get(position).title
        holder.binding.image.visibility = View.VISIBLE
        Glide.with(context).load(newsList.get(position).urlToImage).into(holder.binding.image)
        holder.binding.root.setOnClickListener{
            newsItemClicked.onNewsItemClicked(newsList.get(position))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(updatedList : List<Article>){
        newsList.clear()
        newsList.addAll(updatedList)
        notifyDataSetChanged()
    }
}

interface NewsItemClicked{
    fun onNewsItemClicked(news: Article)
}