package com.fcenesiz.moviemvvm.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.fcenesiz.moviemvvm.data.repository.NetworkState
import com.fcenesiz.moviemvvm.data.vo.Movie
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivityViewModel(
    private val movieRepository : MoviePageListRepository
) :ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }
    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty() : Boolean{
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}