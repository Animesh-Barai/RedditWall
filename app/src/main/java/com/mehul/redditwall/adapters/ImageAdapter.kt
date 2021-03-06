package com.mehul.redditwall.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.objects.BitURL

class ImageAdapter internal constructor(private val context: Context,
                                        private var images: ArrayList<BitURL>?) :
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private val inflater: LayoutInflater
    private val width: Int
    private val height: Int
    private val scale: Int

    init {
        val dims = MainActivity.getDimensions(context)
        width = dims[0]
        height = dims[1]
        scale = (context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)!!.getInt(SettingsActivity.LOAD_SCALE, 2) + 1) * 2
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = inflater.inflate(R.layout.card_image, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (images != null) {
            val current = images!![position]
            if (current.hasGif()) {
                Glide.with(context).asGif().load(current.url).override(width / scale, height / 4).into(holder.image)
            } else {
                try {
                    if (current.img == null || (current.img != null && (current.img as Bitmap).isRecycled)) {
                        Glide.with(context).load(current.url).placeholder(ColorDrawable(Color.GRAY))
                                .override(width / scale, height / 4).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop().into(holder.image)
                        if (holder.image.drawable != null && holder.image.drawable !is ColorDrawable) {
                            Log.e("SAVED", "saved image $position")
                            images!![position].img = (holder.image.drawable as BitmapDrawable).bitmap
                        }
                    } else {
                        holder.image.setImageBitmap(current.img)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("FAIL", "Recycler error")
                    Glide.with(context).load(current.url).placeholder(ColorDrawable(Color.GRAY))
                            .override(width / scale, height / 4).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop().into(holder.image)
                    if (holder.image.drawable != null && holder.image.drawable !is ColorDrawable) {
                        Log.e("SAVED", "saved image $position")
                        images!![position].img = (holder.image.drawable as BitmapDrawable).bitmap
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return images!!.size
    }

    fun setList(list: ArrayList<BitURL>) {
        images = list
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_holder)
    }
}
