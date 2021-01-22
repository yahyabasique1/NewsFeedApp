package com.yahya.newsfeedapp.view.newslist.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yahya.newsfeedapp.R
import com.yahya.newsfeedapp.extensions.loadUrl
import com.yahya.newsfeedapp.repository.remote.NewsListModel
import com.yahya.newsfeedapp.view.newslist.NewsListFragmentDirections
import kotlinx.android.synthetic.main.item_news_list.view.*

class NewsListAdapter:PagingDataAdapter<NewsListModel,RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<NewsListModel>() {
            override fun areItemsTheSame(oldItem: NewsListModel, newItem: NewsListModel) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: NewsListModel, newItem: NewsListModel) =
                oldItem.title == newItem.title
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? NewsListViewHolder)?.bind(item = getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewsListViewHolder.getInstance(parent)

    }

    class NewsListViewHolder(view:View): RecyclerView.ViewHolder(view) {
        companion object {
            //get instance of the NewsListImageViewHolder
            fun getInstance(parent: ViewGroup): NewsListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_news_list, parent, false)
                return NewsListViewHolder(view)
            }
        }


        fun bind(item: NewsListModel?) {
            itemView.apply {
                ivNews.loadUrl(item?.urlToImage?:"")
                tvChannel.text=item?.source?.name ?:""
                tvDateAndSource.text=item?.publishedAt?.substring(0,10)?:""
                textView.text=item?.title?:""

                setOnClickListener {
                    if(item?.description?.isNotEmpty()!!) {
                        val dire = NewsListFragmentDirections.actionNewsListFragmentToNewsDetail(item
                                ?: NewsListModel())
                        it.findNavController().navigate(dire)
                    }
                }

            }

        }
    }
}