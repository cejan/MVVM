package com.fpa.picsum.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fpa.picsum.R
import com.fpa.picsum.viewmodel.ViewActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_viewactivity.*


@AndroidEntryPoint
class ViewActivity : AppCompatActivity() {

    lateinit var viewModel: ViewActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewactivity)

        viewModel  = ViewModelProvider(this).get(ViewActivityViewModel::class.java)

        var bundle :Bundle ?=intent.extras
        val author = bundle?.getString("Author_value")
        val url = bundle?.getString("url_value")

        if(author!="")
            initViewModel(author.toString(),url.toString())

    }

    private fun initViewModel(author: String, url: String) {
     //   viewModel.getdata(id).observe(this, Observer {
            Glide.with(imageView)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.no_available_image)
                .centerCrop()
                .into(imageView)
            textView_author.text = author

      //  })
    }


}