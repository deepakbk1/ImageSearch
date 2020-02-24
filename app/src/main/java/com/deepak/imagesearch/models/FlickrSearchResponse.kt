package com.deepak.imagesearch.models

data class FlickrSearchResponse(
    val photos: FlickrPhotoList,
    val stat: String
)
