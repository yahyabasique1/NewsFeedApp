package com.yahya.newsfeedapp.di.module

import androidx.paging.ExperimentalPagingApi
import com.yahya.newsfeedapp.MainActivity
import com.yahya.newsfeedapp.di.ViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@ExperimentalPagingApi
@Module(includes = [ViewModelModule::class])
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity


}