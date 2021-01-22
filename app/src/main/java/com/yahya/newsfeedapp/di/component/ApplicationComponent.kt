package com.yahya.newsfeedapp.di.component

import android.app.Application
import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.yahya.newsfeedapp.NewsApplication
import com.yahya.newsfeedapp.di.ApplicationContext
import com.yahya.newsfeedapp.di.ViewModelModule
import com.yahya.newsfeedapp.di.module.ActivityModule
import com.yahya.newsfeedapp.di.module.ApiInterfaceModule
import com.yahya.newsfeedapp.di.module.ApplicationModule
import com.yahya.newsfeedapp.di.module.DataBaseModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@ExperimentalPagingApi
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class,
        DataBaseModule::class,
        ActivityModule::class,
        ApiInterfaceModule::class
    ]
)
interface ApplicationComponent {


    fun inject(testApplication: NewsApplication)


    @ApplicationContext
    fun getContext(): Context

    fun application(): Application

//    fun apiInterface(): ApiInterface


}