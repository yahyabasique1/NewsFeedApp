package com.yahya.newsfeedapp.data


import com.yahya.newsfeedapp.repository.remote.*
import javax.inject.Inject

/* Works with the News API to get data. */
class NewsRemoteDataSource @Inject constructor(private val service: ApiInterface) : BaseDataSource() {

    suspend fun fetchNewsList(query:String="Cristiano Ronaldo",apiKey : String, page : Int, pageSize : Int ) : ResponseResult<NewsListResponse> {
        return getResult { service.getTopNewsList("d2faa7e768114a1baa203c73224a3523", page,pageSize, query) }
    }
}
//f7d11217cf16472c8db908bee399e577
//58fd5ec4008b428cb5f8860843b91140
//d2faa7e768114a1baa203c73224a3523
