package com.paigesoftware.androidrecyclerviewpagination

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val dataSet = ArrayList<String?>()
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        populateData()
        setAdapter()

    }


    private fun populateData() {

        var i = 0
        while(i < 10) {
            dataSet.add("Item $i")
            i++
        }

        recyclerview.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as? LinearLayoutManager

                if(!isLoading) {
                    if(linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == dataSet.size - 2)
                    isLoading = true
                    getMoreData()
                }

            }

        })

    }

    private fun getMoreData() {
        dataSet.add(null)
        recyclerViewAdapter.notifyItemInserted(dataSet.size - 1)
        dataSet.removeAt(dataSet.size - 1)
        var currentSize = dataSet.size
        val nextSize = currentSize + 10
        while(currentSize < nextSize) {
            dataSet.add("Item $currentSize")
            currentSize++
        }
        recyclerViewAdapter.notifyDataSetChanged()
        isLoading = false
    }

    private fun setAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter(dataSet)
        recyclerview.adapter = recyclerViewAdapter
        recyclerview.layoutManager = LinearLayoutManager(this)
    }



}