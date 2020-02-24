package com.deepak.imagesearch.data

import androidx.paging.DataSource
import com.deepak.imagesearch.models.FlickrPhoto
import com.deepak.imagesearch.network.FlickrApi

class PhotoDataFactory(private val flickrApi: FlickrApi,
                       private val query: String) : DataSource.Factory<Int, FlickrPhoto>() {

    override fun create(): DataSource<Int, FlickrPhoto> {
        return PhotoDataSource(flickrApi, query)
    }
}
