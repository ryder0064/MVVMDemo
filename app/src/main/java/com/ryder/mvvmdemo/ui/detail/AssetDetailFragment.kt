package com.ryder.mvvmdemo.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ryder.mvvmdemo.R
import com.ryder.mvvmdemo.ui.MainActivity
import com.ryder.mvvmdemo.ui.list.AssetsViewModel
import com.ryder.mvvmdemo.util.TAG
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AssetDetailFragment : Fragment() {
    private val args: AssetDetailFragmentArgs by navArgs()
    private val viewModel: AssetsViewModel by sharedViewModel()
    private lateinit var assetImage: ImageView
    private lateinit var assetName: TextView
    private lateinit var assetDesc: TextView
    private lateinit var loadingLayout: FrameLayout
    private lateinit var permalinkBtn: Button
    private var permalink: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_asset_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tokenId = args.tokenId
        val contractAddress = args.contractAddress
        val name = args.name
        (requireActivity() as MainActivity).title = name

        assetImage = view.findViewById(R.id.image_asset)
        assetName = view.findViewById(R.id.text_name)
        assetDesc = view.findViewById(R.id.text_desc)
        loadingLayout = view.findViewById(R.id.layout_loading)
        permalinkBtn = view.findViewById(R.id.permalink)

        viewModel.refreshAssetDetail(contractAddress, tokenId)
        viewModel.getSnackBarMessage().observe(viewLifecycleOwner) { text ->
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).setAction(
                "Retry"
            ) {
                viewModel.refreshAssetDetail(contractAddress, tokenId)
            }.show()
            viewModel.clearSnackBar()
        }
        viewModel.getAssetDetail().observe(viewLifecycleOwner) { detail ->
            assetName.text = detail.name
            assetDesc.text = detail.description
            permalink = detail.permalink

            Picasso.get()
                .load(detail.imageUrl)
                .fit()
                .centerInside()
                .into(assetImage)
        }
        viewModel.getLoadingStatus().observe(viewLifecycleOwner) { isRefreshing ->
            if (isRefreshing) {
                loadingLayout.visibility = View.VISIBLE
                permalinkBtn.isClickable = false
            } else {
                loadingLayout.visibility = View.GONE
                permalinkBtn.isClickable = true
            }
        }

        permalinkBtn.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(permalink)
                )
            )
        }
    }
}