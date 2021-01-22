package com.yahya.newsfeedapp.data


import com.yahya.newsfeedapp.repository.remote.*
import javax.inject.Inject
const val API_KEY="enter_your_api_key_here"
/* Works with the News API to get data. */
class NewsRemoteDataSource @Inject constructor(private val service: ApiInterface) : BaseDataSource() {

    suspend fun fetchNewsList(query:String="Cristiano Ronaldo",apiKey : String, page : Int, pageSize : Int ) : ResponseResult<NewsListResponse> {
        return getResult { service.getTopNewsList(API_KEY, page,pageSize, query) }
    }
}
