package com.fcenesiz.moviemvvm.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fcenesiz.moviemvvm.data.api.IMovieDB
import com.fcenesiz.moviemvvm.data.api.POST_PAGE_RANGE
import com.fcenesiz.moviemvvm.data.repository.MovieDataSource
import com.fcenesiz.moviemvvm.data.repository.MovieDataSourceFactory
import com.fcenesiz.moviemvvm.data.repository.NetworkState
import com.fcenesiz.moviemvvm.data.vo.Movie
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MoviePageListRepository(
    private val apiService: IMovieDB
) {

    lateinit var moviePagedList : LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>>{
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PAGE_RANGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState() : LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource,
            MovieDataSource::networkState
        )
    }

}