package com.yahya.newsfeedapp.data

import android.util.Log
import androidx.paging.PagingSource
import com.yahya.newsfeedapp.repository.remote.*
import retrofit2.HttpException
import java.io.IOException

/*

 */

const val DEFAULT_PAGE_INDEX = 1
const val DEFAULT_PAGE_SIZE = 5

class NewsFeedPagingSource(val newsRemoteDataSource: NewsRemoteDataSource) : PagingSource<Int, NewsListModel>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsListModel> {

        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = newsRemoteDataSource.fetchNewsList("","58d482512a2e430fa414efe8c8747fc1", page, params.loadSize)
            Log.e("RESP","$response status ${response.status}")

            when(response.status){
                    ResponseResult.Status.SUCCESS ->{
                        LoadResult.Page(
                            response.data?.articles!!,
                            prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                            nextKey = if (response.data!!.articles.isEmpty()) null else page + 1
                        )
                    }

                ResponseResult.Status.ERROR ->{
                    LoadResult.Error(throwable = Throwable(response.message))
                }
                else -> {
                    LoadResult.Error(throwable = Throwable(response.message+""))

                }
            }

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }


}