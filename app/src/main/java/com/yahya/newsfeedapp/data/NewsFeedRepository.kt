package com.yahya.newsfeedapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import androidx.paging.rxjava2.observable
import com.yahya.newsfeedapp.repository.local.AppDatabase
import com.yahya.newsfeedapp.repository.local.LocalInjector
import com.yahya.newsfeedapp.repository.remote.ApiInterface
import com.yahya.newsfeedapp.repository.remote.NewsListModel
import com.yahya.newsfeedapp.repository.remote.RemoteInjector
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class NewsFeedRepository @Inject constructor(
        private val newsRemoteDataSource: NewsRemoteDataSource ,
        private val appDatabase: AppDatabase
) {

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 5
    }

    /**
     * let's define page size, page size is the only required param, rest is optional
     */
    fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }


    /**
     * calling the paging source to give results from api calls
     * and returning the results in the form of flow [Flow<PagingData<NewsListModel>>]
     * since the [PagingDataAdapter] accepts the [PagingData] as the source in later stage
     */
    fun letNewsFeedFlow(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<NewsListModel>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { NewsFeedPagingSource(newsRemoteDataSource) }
        ).flow
    }

    //for rxjava users
    fun letNewsFeedObservable(pagingConfig: PagingConfig = getDefaultPageConfig()): Observable<PagingData<NewsListModel>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { NewsFeedPagingSource(newsRemoteDataSource) }
        ).observable
    }

    //for live data users
    fun letNewsFeedLiveData(pagingConfig: PagingConfig = getDefaultPageConfig()): LiveData<PagingData<NewsListModel>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { NewsFeedPagingSource(newsRemoteDataSource) }
        ).liveData
    }

    fun letNewsFeedLiveDataDb(query:String="Cristiano Ronaldo", pagingConfig: PagingConfig = getDefaultPageConfig()): LiveData<PagingData<NewsListModel>> {
        if (appDatabase == null) throw IllegalStateException("Database is not initialized")

        val pagingSourceFactory = { appDatabase.getNewsModelDao().getAllNewsModel() }

//        val pg={PagingSource.LoadResult.Page(appDatabase.getNewsModelDao().getAllNewsModel(),null,null )}
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = NewsMediator(query,newsRemoteDataSource,appDatabase)
        ).liveData
    }
}