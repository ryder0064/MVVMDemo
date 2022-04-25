package com.ryder.mvvmdemo.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryder.mvvmdemo.R
import com.ryder.mvvmdemo.data.model.AssetListResponse
import com.squareup.picasso.Picasso

class AssetsAdapter(private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<AssetsAdapter.ViewHolder>() {

    private var assetsList: MutableList<AssetListResponse.Asset> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.assets_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (assetsList.isNotEmpty()) holder.bind(assetsList[position], itemClickListener)
    }

    override fun getItemCount(): Int = assetsList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val assetName: TextView = itemView.findViewById(R.id.text_name)
        private val assetImage: ImageView = itemView.findViewById(R.id.image_asset)

        fun bind(assetList: AssetListResponse.Asset, itemClickListener: OnItemClickListener) {
            assetName.text = assetList.name
            Picasso.get()
                .load(assetList.imageUrl)
                .fit()
                .centerInside()
                .tag(itemView.context)
                .into(assetImage)

            itemView.setOnClickListener {
                if (adapterPosition != -1) itemClickListener.onItemClicked(
                    assetList.tokenId, assetList.assetContract.address, assetList.name
                )
            }
        }
    }

    fun setData(list: List<AssetListResponse.Asset>) {
        val positionStart = assetsList.size
        assetsList.addAll(list)
        notifyItemRangeChanged(positionStart, list.size)
    }

    fun clear() {
        assetsList.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClicked(tokenId: String, contractAddress: String, name: String)
    }
}
