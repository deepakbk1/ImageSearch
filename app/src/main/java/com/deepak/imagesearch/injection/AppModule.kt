package com.deepak.imagesearch.injection

import com.deepak.imagesearch.injection.scopes.PerApp
import com.deepak.imagesearch.network.FlickrApi
import com.deepak.imagesearch.network.getBaseUrl
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@dagger.Module
class AppModule {

    @Provides
    @PerApp
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @PerApp
    fun provideFlickrService(retrofit: Retrofit): FlickrApi {
        return retrofit.create(FlickrApi::class.java)
    }
}
