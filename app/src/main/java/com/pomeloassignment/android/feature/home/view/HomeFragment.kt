package com.pomeloassignment.android.feature.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pomeloassignment.android.databinding.FragmentHomeBinding
import com.pomeloassignment.android.feature.home.viewmodel.HomeViewModel
import com.pomeloassignment.android.feature.home.viewmodel.HomeViewState
import com.pomeloassignment.android.db.ArticleEntity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private val vm: HomeViewModel by viewModels { vmFactory }
    private lateinit var homeListAdapter: HomeListingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initSearch()
        vm.fetchNews()

        //observer for state changes
        vm.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is HomeViewState.FetchArticlesState -> {
                    when {
                        state.data != null -> {
                            binding.loading.visibility = View.GONE
                            binding.resultListing.visibility = View.VISIBLE
                            homeListAdapter.addItems(state.data)
                        }
                        state.error != null -> {
                            //show snackbar
                            binding.loading.visibility = View.GONE
                            binding.resultListing.visibility = View.VISIBLE
                        }
                        state.isLoading -> {
                            //show loading screen
                            binding.loading.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    private fun initList() {
        binding.resultListing.apply {
            val clickListener: (article: ArticleEntity, pos: Int) -> Unit = { article, pos ->
                val navController = findNavController()
                val action = HomeFragmentDirections.actionDetailFromHome(article)
                navController.navigate(action)
            }
            homeListAdapter = HomeListingAdapter(clickListener)
            adapter = homeListAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun initSearch() {
        binding.searchBox.addTextChangedListener {
            if (isAdded && isVisible) {
                val editable = binding.searchBox.editableText ?: return@addTextChangedListener
                if (editable.length > 3) vm.query(editable.toString().trim())
                //if search goes empty then bring back top news back
                if (editable.isEmpty()) vm.fetchNews()
            }
        }
    }
}