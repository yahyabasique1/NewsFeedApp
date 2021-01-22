package com.yahya.newsfeedapp.data

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.yahya.newsfeedapp.repository.local.AppDatabase
import com.yahya.newsfeedapp.repository.local.RemoteKey
import com.yahya.newsfeedapp.repository.remote.NewsListModel
import com.yahya.newsfeedapp.repository.remote.ResponseResult
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class NewsMediator(val query: String="Cristiano Ronaldo",val newsRemoteDataSource: NewsRemoteDataSource, val appDatabase: AppDatabase) :
    RemoteMediator<Int, NewsListModel>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsListModel>
    ): MediatorResult {

        val pagedKeyData=getKeyPageData(loadType,state)
        Log.e("PAGEDDATA","$pagedKeyData")
        val page=when(pagedKeyData){
                is MediatorResult.Success ->{
                    return pagedKeyData
                }

            else -> {
                 pagedKeyData as Int
            }
        }

        try {
            val response=newsRemoteDataSource.fetchNewsList(query = query,"58d482512a2e430fa414efe8c8747fc1",page,state.config.pageSize)
            val isEndofList=response.data?.articles?.isEmpty()
            when(response.status){
                ResponseResult.Status.SUCCESS ->{
                    appDatabase.withTransaction {
                        if(loadType==LoadType.REFRESH){
                            appDatabase.getRepoDao().clearRemoteKeys()
                            appDatabase.getNewsModelDao().clearAllNews()
                        }

                        val prevKey=if(page== DEFAULT_PAGE_INDEX) null else page-1
                        val nextKey=if(isEndofList!!) null else page+1

                        val keys=response.data.articles.map {
                            RemoteKey(newsTitle = it.title, prevKey = prevKey, nextKey = nextKey)

                        }

                        appDatabase.getRepoDao().insertAll(keys)
                        appDatabase.getNewsModelDao().insertAll(response.data.articles)
                    }

                    return MediatorResult.Success(endOfPaginationReached = isEndofList!!)

                }

                ResponseResult.Status.ERROR ->{
                    return MediatorResult.Error(Throwable(response.message))

                }
            }

           return MediatorResult.Error(Throwable(response.message))
        }catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }


    /**
     * this returns the page key or the final end of list success result
     */
    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, NewsListModel>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
    }


    private suspend fun getLastRemoteKey(state: PagingState<Int, NewsListModel>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { newsfeed -> appDatabase.getRepoDao().remoteKeysNewsTitle(newsfeed.title) }
    }


    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, NewsListModel>): RemoteKey? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let {
                appDatabase.getRepoDao().remoteKeysNewsTitle(it.title)
            }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, NewsListModel>): RemoteKey? {
        return state.anchorPosition?.let { pos ->
            state.closestItemToPosition(pos)?.title?.let { title ->
                appDatabase.getRepoDao().remoteKeysNewsTitle(title)
            }
        }
    }
}