package com.deepak.imagesearch.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.deepak.imagesearch.models.FlickrPhoto
import com.deepak.imagesearch.network.FlickrApi

class FlickrPhotoViewModel : ViewModel() {

    private var liveData: LiveData<PagedList<FlickrPhoto>>? = null

    fun getPhotos(flickrApi: FlickrApi, query: String, resetData : Boolean = false) : LiveData<PagedList<FlickrPhoto>> {
        if (liveData == null || resetData) {
            val dataFactory = PhotoDataFactory(flickrApi, query)
            val config = PagedList.Config.Builder()
                    .setPageSize(1)
                    .setInitialLoadSizeHint(10)
                    .setEnablePlaceholders(false)
                    .build()

            liveData = LivePagedListBuilder<Int, FlickrPhoto>(dataFactory, config)
                    .setInitialLoadKey(1)
                    .build()
        }
        return liveData as LiveData<PagedList<FlickrPhoto>>
    }
}
