package com.fpa.picsum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fpa.picsum.R
import com.fpa.picsum.model.PicSum
import kotlinx.android.synthetic.main.reporitory_list_row.view.*



class RecyclerAdapter : PagingDataAdapter<PicSum,
        RecyclerAdapter.MyViewHolder>(diffCallback) {

    private lateinit var mlistener : OnItemClickListener

    interface  OnItemClickListener{
        fun onItemClick(Author: String,url: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mlistener = listener
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PicSum>() {
            override fun areItemsTheSame(oldItem: PicSum, newItem: PicSum): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PicSum, newItem: PicSum): Boolean {
                return oldItem == newItem
            }
            override fun getChangePayload(oldItem: PicSum, newItem: PicSum): Any {
                return Any()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.reporitory_list_row, parent, false)
        return MyViewHolder(view, mlistener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

                holder.bind(getItem(position)!!)

    }

    inner class MyViewHolder(view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view)  {
        private val imageAvatarUrl = view.image_avatar_url
        private val textViewAuthor = view.textView_author
        private var textAuthor =""
        private var textUrl =""
        init {
            itemView.setOnClickListener {
                listener.onItemClick(textAuthor,textUrl)
            }
        }
        fun bind(data: PicSum) {
            textAuthor = data.author.toString()
            textUrl = data.download_url.toString()

            textViewAuthor.text = data.author
            Glide.with(imageAvatarUrl)
                .load(data.download_url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.no_available_image)
                .into(imageAvatarUrl)
        }
    }
}