package com.fpa.picsum.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpa.picsum.R
import com.fpa.picsum.adapter.RecyclerAdapter
import com.fpa.picsum.databinding.ActivityMainBinding
import com.fpa.picsum.viewmodel.MainActivityViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewAdapter: RecyclerAdapter
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initMainViewModel()

        recyclerViewAdapter.setOnItemClickListener(object : RecyclerAdapter.OnItemClickListener{
            override fun onItemClick(Author: String,url: String) {

                val intent = Intent(this@MainActivity, ViewActivity::class.java)
                intent.putExtra("Author_value", Author)
                intent.putExtra("url_value", url)
                startActivity(intent)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_refresh-> {
                recyclerViewRefresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun recyclerViewRefresh(){
        lifecycleScope.launch {
            viewModel.listData.collect {
                recyclerViewAdapter.submitData(it)
            }

        }
    }

    private fun initViewModel() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            recyclerViewAdapter = RecyclerAdapter()
            adapter =recyclerViewAdapter

        }

    }

    private fun initMainViewModel() {
        lifecycleScope.launch {
            viewModel.dataSource.collectLatest {
                recyclerViewAdapter.submitData(it)
            }
        }
    }
}