package com.yahya.newsfeedapp

import android.app.Activity
import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.yahya.newsfeedapp.di.component.ApplicationComponent
import com.yahya.newsfeedapp.di.component.DaggerApplicationComponent
import com.yahya.newsfeedapp.di.module.ApplicationModule
import com.yahya.newsfeedapp.di.module.DataBaseModule
import com.yahya.newsfeedapp.repository.local.AppDatabase
import com.yahya.newsfeedapp.repository.local.LocalInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

@ExperimentalPagingApi
class NewsApplication  : Application(),HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    protected lateinit var mApplicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
//        LocalInjector.appDatabase = AppDatabase.getInstance(this@NewsApplication)

        mApplicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .dataBaseModule(DataBaseModule(this))
            .build()
        mApplicationComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }

}