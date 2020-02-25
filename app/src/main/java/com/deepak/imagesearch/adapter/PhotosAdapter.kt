package com.deepak.imagesearch.adapter

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.deepak.imagesearch.R
import com.deepak.imagesearch.models.FlickrPhoto
import com.deepak.imagesearch.network.getPhotoUrl
import com.deepak.imagesearch.adapter.PhotosAdapter.PhotoHolder
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide

class PhotosAdapter : PagedListAdapter<FlickrPhoto, PhotoHolder>(getFlickrPhotoDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PhotoHolder(inflater.inflate(R.layout.photo_item, null))
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val photo: FlickrPhoto = getItem(position) ?: return
        holder.image?.let {
            Glide.with(holder.itemView.context)
                .load(getPhotoUrl(photo.farm, photo.server, photo.id, photo.secret))
                .into(it)
        }
    }

    class PhotoHolder(itemView: View) : ViewHolder(itemView) {
        var image: ImageView? = null

        init {
            image = itemView.findViewById(R.id.image)
        }
    }

    companion object {
        fun getFlickrPhotoDiff() = object : DiffUtil.ItemCallback<FlickrPhoto>() {
            override fun areItemsTheSame(oldItem: FlickrPhoto, newItem: FlickrPhoto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FlickrPhoto, newItem: FlickrPhoto): Boolean {
                return oldItem == newItem
            }
        }
    }
}
