package com.fcenesiz.moviemvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.fcenesiz.moviemvvm.data.api.IMovieDB
import com.fcenesiz.moviemvvm.data.repository.MovieDetailsNetworkDataSource
import com.fcenesiz.moviemvvm.data.repository.NetworkState
import com.fcenesiz.moviemvvm.data.vo.MovieDetails
import io.reactivex.rxjava3.disposables.CompositeDisposable


class MovieDetailsRepository(
    private val apiService: IMovieDB
) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails>{
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }
}