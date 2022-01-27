package com.pomeloassignment.android.feature.detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.pomeloassignment.android.R
import com.pomeloassignment.android.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.wait
import android.webkit.WebViewClient

@AndroidEntryPoint
class DetailFragment : Fragment() {

    val args by navArgs<DetailFragmentArgs>()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWebView()
        //url loading webview
        val url = args.articleData.url ?: return
        binding.detailWebview.loadUrl(url)
    }

    private fun initWebView() {
        val webSettins = binding.detailWebview.settings
        webSettins.loadsImagesAutomatically = true
        webSettins.javaScriptEnabled = true
        binding.detailWebview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.detailWebview.webViewClient = WebViewClient()
    }
}