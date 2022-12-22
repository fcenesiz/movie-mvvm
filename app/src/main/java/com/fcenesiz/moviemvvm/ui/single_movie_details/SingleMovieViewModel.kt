package com.fcenesiz.moviemvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fcenesiz.moviemvvm.data.repository.NetworkState
import com.fcenesiz.moviemvvm.data.vo.MovieDetails
import io.reactivex.rxjava3.disposables.CompositeDisposable


class SingleMovieViewModel(
    private val movieRepository : MovieDetailsRepository,
    private val movieId: Int
) :ViewModel(){
    private val compositeDisposable = CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}