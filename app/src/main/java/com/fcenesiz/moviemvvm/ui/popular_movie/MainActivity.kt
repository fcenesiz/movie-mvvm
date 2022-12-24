package com.fcenesiz.moviemvvm.ui.popular_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.fcenesiz.moviemvvm.data.api.MovieClient
import com.fcenesiz.moviemvvm.data.repository.NetworkState
import com.fcenesiz.moviemvvm.databinding.ActivityMainBinding

// api_key: 398ea00d8c7f65d3aff1699dac3ce42f
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var moviePageListRepository: MoviePageListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = MovieClient.getClient()
        moviePageListRepository = MoviePageListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = PopularMoviePagedListAdapter()
        val gridLayoutManager = GridLayoutManager(this, 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = movieAdapter.getItemViewType(position)
                    if (viewType == movieAdapter.MOVIE_VIEW_TYPE) {
                        return 1
                    } else {
                        return 3
                    }
                }
            }
        }

        binding.recyclerViewMovies.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        viewModel.apply {
            moviePagedList.observe(this@MainActivity, Observer {
                movieAdapter.submitList(it)
            })
            networkState.observe(this@MainActivity, Observer {
                binding.apply {
                    progressBarPopular.visibility =
                        if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE
                        else View.GONE
                    textViewErrorPopular.visibility =
                        if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE
                        else View.GONE
                }

                if(!viewModel.listIsEmpty()){
                    movieAdapter.setNetworkState(it)
                }
            })
        }

    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainActivityViewModel(moviePageListRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }
}