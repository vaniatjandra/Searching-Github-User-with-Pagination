package com.mvhousedeveloper.tiketdotcomtest.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.mvhousedeveloper.tiketdotcomtest.R
import com.mvhousedeveloper.tiketdotcomtest.viewModel.MainViewModel
import com.mvhousedeveloper.tiketdotcomtest.adapter.ListAdapter
import com.mvhousedeveloper.tiketdotcomtest.databinding.ActivityMainBinding
import com.mvhousedeveloper.tiketdotcomtest.di.Injection
import com.mvhousedeveloper.tiketdotcomtest.model.SearchResult

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val adapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory()).get(MainViewModel::class.java)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        binding.list.addItemDecoration(decoration)
        binding.progressBar.visibility = View.GONE

        setupScrollListener()
        initAdapter()

        val query = ""
        if (viewModel.repoResult.value == null) {
            viewModel.searchUser(query)
        }

        initSearch(query)
    }

    private fun initAdapter() {
        binding.list.adapter = adapter
        viewModel.repoResult.observe(this) { result ->
            when (result) {
                is SearchResult.Success -> {
                    showError("", false)
                    showEmptyList(result.data.isEmpty())
                    adapter.submitList(result.data)
                }
                is SearchResult.Error -> {
                    if (result.error.message.toString().isNotEmpty()) {
                        if (binding.searchUser.query.trim().isNotEmpty()) {
                            var errorMsg: String = resources.getString(R.string.error_msg_unknown)
                            if(result.error.localizedMessage.toString().contains("Unable to resolve host"))
                                errorMsg = resources.getString(R.string.error_msg_no_internet)
                            else if(result.error.localizedMessage.toString().contains("Failed to connect"))
                            errorMsg = resources.getString(R.string.error_msg_no_internet)
                            else if(result.error.localizedMessage.toString().contains("timeout"))
                                errorMsg = resources.getString(R.string.error_msg_timeout)
                            else if(result.error.localizedMessage.toString().contains("HTTP 403"))
                                errorMsg = resources.getString(R.string.error_reached_limit)
                            showError(errorMsg, true)
                        }
                    }
                }
            }
        }
    }

    private fun initSearch(query: String) {
        binding.searchUser.setQuery(query, true)

        binding.searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.trim().isNotEmpty())
                {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.emptyList.visibility = View.GONE
                }
                else
                    hideKeyboard(this@MainActivity)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.trim().isNotEmpty()) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.emptyList.visibility = View.GONE
                    updateRepoListFromInput()
                    hideKeyboard(this@MainActivity)
                }
                return true
            }
        })

        binding.searchUser.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                updateRepoListFromInput()
                return true
            }
        })
    }

    private fun updateRepoListFromInput() {
        binding.searchUser.query.trim().let {
            if (it.isNotEmpty()) {
                binding.list.scrollToPosition(0)
                viewModel.searchUser(it.toString())
            }
        }
    }

    private fun showError(query: String, show: Boolean) {
        if(show)
        {
            binding.errorTxtCause.text = query
            binding.errorLayout.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
        else
        {
            binding.errorTxtCause.text = query
            binding.errorLayout.visibility = View.GONE
            binding.list.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
            binding.errorLayout.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.list.visibility = View.VISIBLE
            binding.errorLayout.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupScrollListener() {
        val layoutManager = binding.list.layoutManager as LinearLayoutManager
        binding.list.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }

    // method to hide the keyboard on each search
    fun hideKeyboard(activity: Activity?) {
        activity?.let {
            val view = it.currentFocus ?: return
            val inputManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
