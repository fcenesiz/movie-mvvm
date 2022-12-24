package com.fcenesiz.moviemvvm.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fcenesiz.moviemvvm.data.api.IMovieDB
import com.fcenesiz.moviemvvm.data.vo.Movie
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val apiService: IMovieDB,
    private val compositeDisposable: CompositeDisposable
): DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}