package com.fcenesiz.moviemvvm.ui.single_movie_details

import android.icu.text.NumberFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fcenesiz.moviemvvm.data.api.IMovieDB
import com.fcenesiz.moviemvvm.data.api.MovieClient
import com.fcenesiz.moviemvvm.data.api.POSTER_BASE_URL
import com.fcenesiz.moviemvvm.data.repository.NetworkState
import com.fcenesiz.moviemvvm.data.vo.MovieDetails
import com.fcenesiz.moviemvvm.databinding.ActivitySingleMovieBinding
import java.util.*


class SingleMovieActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySingleMovieBinding
    private lateinit var viewModel : SingleMovieViewModel
    private lateinit var movieRepository : MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra("id", 1)
        val apiService : IMovieDB = MovieClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)
        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.apply {
                progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
                textViewError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
            }
        })
    }

    private fun bindUI(it: MovieDetails){
        binding.apply {
            textViewMovieTitle.text = it.title
            textViewMovieTagLine.text = it.tagline
            textViewReleaseDate.text = it.releaseDate
            textViewRating.text = it.rating.toString()
            textViewRuntime.text = "${it.runtime} minutes"
            textViewMovieOverview.text = it.overview

            val formatCurrency : NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
            textViewBudget.text = formatCurrency.format(it.budget)
            textViewRevenue.text = formatCurrency.format(it.revenue)

            val moviePosterUrl = POSTER_BASE_URL + it.posterPath
            Glide.with(this@SingleMovieActivity)
                .load(moviePosterUrl)
                .into(imageViewMoviePoster)
        }
    }

    private fun getViewModel(movieId: Int) : SingleMovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}