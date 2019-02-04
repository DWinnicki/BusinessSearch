package com.winnicki.businesssearch.ui.search

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.winnicki.businesssearch.R
import com.winnicki.businesssearch.model.Business
import kotlinx.android.synthetic.main.search_item.view.*

class SearchAdapter(
    private val businessList: MutableList<Business>,
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.search_item, parent, false)
        )

    override fun getItemCount() = businessList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(businessList[position], clickListener)

        if (position % 2 == 1) {
            viewHolder.itemView.setBackgroundColor(Color.WHITE)
        } else {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"))
        }
    }

    fun sortByName() {
        businessList.sortBy { it.name }
        notifyDataSetChanged()
    }

    fun sortByRating() {
        businessList.sortByDescending { it.rating }
        notifyDataSetChanged()
    }

    fun sortByDistance() {
        businessList.sortBy { it.distance }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val photo: ImageView = view.imageView
        private val name: TextView = view.textViewName
        private val rating: TextView = view.textViewRating
        private val distance: TextView = view.textViewDistance

        fun bind(item: Business, clickListener: OnItemClickListener) {
            name.text = item.name
            rating.text = itemView.context.resources.getString(R.string.rating_colon_number, item.rating)
            distance.text = itemView.context.resources.getString(R.string.distance_meters, item.distance.toInt())

            if (item.imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .into(photo)
            }

            itemView.setOnClickListener {
                clickListener.onItemClick(item)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Business)
    }
}
