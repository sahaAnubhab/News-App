package com.example.newsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.FragmentSportsNewsViewBinding
import com.example.newsapp.databinding.ItemCategoryBinding

class CategoryRecyclerViewAdapter(private val categoryClickListener : CategoryClickListener) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder>() {

    val list = arrayOf("business", "food", "health", "sports")

    class CategoryViewHolder(val binding: ItemCategoryBinding):  RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        when(position){
            0->{
                holder.binding.imageView.setImageResource(R.drawable.business)
                holder.binding.textView2.text = "BUSINESS"
            }
            1->{
                holder.binding.imageView.setImageResource(R.drawable.science)
                holder.binding.textView2.text = "SCIENCE"
            }
            2->{
                holder.binding.imageView.setImageResource(R.drawable.health)
                holder.binding.textView2.text = "HEALTH"
            }
            3->{
                holder.binding.imageView.setImageResource(R.drawable.sports)
                holder.binding.textView2.text = "SPORTS"
            }
        }

        holder.binding.root.setOnClickListener{
            categoryClickListener.onCategoryClicked(position)
        }
    }
}

interface CategoryClickListener{
    fun onCategoryClicked(position: Int)
}