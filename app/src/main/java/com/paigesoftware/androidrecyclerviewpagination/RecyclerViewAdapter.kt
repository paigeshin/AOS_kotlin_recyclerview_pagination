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