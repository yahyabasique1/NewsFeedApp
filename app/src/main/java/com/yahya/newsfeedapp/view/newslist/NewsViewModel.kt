package com.yahya.newsfeedapp.view.newslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yahya.newsfeedapp.repository.remote.NewsListModel
import com.yahya.newsfeedapp.data.NewsFeedRepository
import javax.inject.Inject

@ExperimentalPagingApi
class NewsViewModel  @Inject constructor(var newsFeedRepository: NewsFeedRepository):ViewModel() {
    /**
     * returning non modified PagingData<NewsListModel> value as opposite to remote view model
     * where we have mapped the coming values into different object
     */


   @ExperimentalPagingApi
   suspend fun fetchDoggoImages(query:String="Biriyani"):LiveData<PagingData<NewsListModel>> =
            newsFeedRepository.letNewsFeedLiveDataDb(query=query).cachedIn(viewModelScope)


}