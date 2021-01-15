1. Create row with Loading UI
2. Implement **getItemViewType** to return loading UI when reached end of recyclerView
3. Add **scrollListener** to start loading new data when about to reach the end 

# RecyclerViewAdapter

- `Think about ViewType`

```kotlin
package com.paigesoftware.androidrecyclerviewpagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class RecyclerViewAdapter(
    private val itemList: List<String?>
): RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    open class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {}
    //3. create loading holder
    class LoadingHolder(view: View): MyViewHolder(view) {}

    //1. create view type constants
    companion object {
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_ITEM = 1
    }

    //4. return loading holder according to VIEW TYPE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if(viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
            MyViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.textview.text = itemList[holder.adapterPosition]
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    //2. return vie type
    override fun getItemViewType(position: Int): Int {
        return if(itemList[position] == null) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

}
```

# MainActivity

- `linearLayoutManager.findLastCompletelyVisibleItemPosition() == dataSet.size - 2`

```kotlin
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
```

```kotlin
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
```