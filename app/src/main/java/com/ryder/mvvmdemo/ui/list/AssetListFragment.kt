package com.ryder.mvvmdemo.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.ryder.mvvmdemo.R
import com.ryder.mvvmdemo.util.EndlessRecyclerViewScrollListener
import com.ryder.mvvmdemo.util.LIMIT
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AssetListFragment : Fragment() {
    private lateinit var viewAdapter: AssetsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val viewModel: AssetsViewModel by sharedViewModel()
    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_asset_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        viewAdapter = AssetsAdapter(object : AssetsAdapter.OnItemClickListener {
            override fun onItemClicked(tokenId: String, contractAddress: String, name: String) {
                Log.d("TAG", "onItemClicked: ")
                val action =
                    AssetListFragmentDirections.actionAssetListFragmentToAssetDetailFragment(
                        tokenId,
                        contractAddress,
                        name
                    )
                view.findNavController().navigate(action)
            }
        })

        viewModel.getAssetList().observe(viewLifecycleOwner) { assets ->
            viewAdapter.setData(assets)
        }

        viewModel.getLoadingStatus().observe(viewLifecycleOwner) { isRefreshing ->
            swipeRefreshLayout.isRefreshing = isRefreshing
        }

        viewModel.getSnackBarMessage().observe(viewLifecycleOwner) { text ->
            Snackbar.make(swipeRefreshLayout, text, Snackbar.LENGTH_LONG).setAction(
                "Retry"
            ) {
                viewAdapter.clear()
                viewModel.refreshAssetList()
            }.show()
            viewModel.clearSnackBar()
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewAdapter.clear()
            viewModel.refreshAssetList()
        }
        setRecyclerViewListeners()
    }

    private fun setRecyclerViewListeners() {
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        endlessRecyclerViewScrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.refreshAssetList(page * LIMIT)
            }
        }
        recyclerView.adapter = viewAdapter
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener)
    }
}