package com.yahya.newsfeedapp.di.module


import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.yahya.newsfeedapp.data.NewsRemoteDataSource
import com.yahya.newsfeedapp.repository.remote.ApiInterface
import com.yahya.newsfeedapp.repository.remote.ApiInterface.Companion.ENDPOINT
import javax.inject.Singleton


@Module(includes = [OkHttpModule::class])
class ApiInterfaceModule {

    @Provides
    fun apiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)

    }

    @Provides
    fun retrofitClient(okHttpClient: OkHttpClient,gsonConverterFactory: GsonConverterFactory,rxJava2CallAdapterFactory: RxJava2CallAdapterFactory):Retrofit{
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ENDPOINT)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()


    }

    @Provides
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }


    @Provides
    fun gsonConverter(gson: Gson):GsonConverterFactory{
        return GsonConverterFactory.create(gson)
    }


    @Provides
    fun rxjavaAdapter():RxJava2CallAdapterFactory{
        return RxJava2CallAdapterFactory.create()
    }

    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(apiInterface: ApiInterface)
            = NewsRemoteDataSource(apiInterface)
}