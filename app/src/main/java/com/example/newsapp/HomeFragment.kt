package com.example.newsapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsfresh.MySingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment(), CategoryClickListener, NewsItemClicked {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mNewsAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    var curr_position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setCategory()
        mNewsAdapter = NewsAdapter(this, requireContext())
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.newsRV.layoutManager = layoutManager
        binding.newsRV.adapter = mNewsAdapter
        setNews("business")

        return binding.root

    }

    private fun setNews(type: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        lifecycleScope.launch(Dispatchers.IO) {
            val api = retrofit.create(NewsApi::class.java)
            val authToken = "9ef9ceecbd524c1896f12984e268ccb7"

            val response = api.getBusinessNews(authToken, "in", type)
            if(response.isSuccessful){
                val data = response.body()
                if(data != null){
                    Log.d("HelloNews", data.toString())
                    withContext(Dispatchers.Main) {
                        mNewsAdapter.updateList(data.articles)
                    }
                }
            }
            else{
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    private fun fetchData(category : String){
//        val url = "https://newsapi.org/v2/top-headlines?country=in&category=science&piKey=1f4a12d2698e432ea9cf18126dcc7acd"
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.GET,
//            url,
//            null,
//            Response.Listener {
//                val newsJsonArray = it.getJSONArray("articles")
//                val newsArray = ArrayList<Article>()
//                for(i in 0 until newsJsonArray.length()) {
//                    val newsJsonObject = newsJsonArray.getJSONObject(i)
//                    val news = News(
//                        newsJsonObject.getString("title"),
//                        newsJsonObject.getString("author"),
//                        newsJsonObject.getString("url"),
//                        newsJsonObject.getString("urlToImage")
//                    )
//                    newsArray.add(news)
//                }
//
//                mNewsAdapter.updateList(newsArray)
//            },
//            Response.ErrorListener {
//
//            }
//        )
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
//    }

    private fun setCategory() {
        val adapter = CategoryRecyclerViewAdapter(this)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRV.layoutManager = layoutManager
        binding.categoriesRV.adapter = adapter
    }

    override fun onCategoryClicked(position: Int) {
        if(position == curr_position) return
        when(position){
            0 ->{
                setNews("business")
            }
            1 -> {
                setNews("science")
            }
            2->{
                setNews("health")
            }
            3->{
                setNews("sports")
            }
        }
    }

    override fun onNewsItemClicked(news: Article) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(news.url))
    }


}