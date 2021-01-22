package com.yahya.newsfeedapp.di.module

import androidx.paging.ExperimentalPagingApi
import com.yahya.newsfeedapp.di.FragmentScope
import com.yahya.newsfeedapp.view.newsdetail.NewsDetail
import com.yahya.newsfeedapp.view.newslist.NewsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@ExperimentalPagingApi
@Module
abstract class FragmentModule {

    @FragmentScope(NewsListFragment::class)
    @ContributesAndroidInjector
    abstract fun getNewsListFragment():NewsListFragment

    @FragmentScope(NewsDetail::class)
    @ContributesAndroidInjector
    abstract fun getNewsDetail():NewsDetail

}