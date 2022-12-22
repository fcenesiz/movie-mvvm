package com.fcenesiz.moviemvvm.data.api

import com.fcenesiz.moviemvvm.data.vo.MovieDetails
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface IMovieDB {

    @GET("movie/{movie_id}")
    fun getMovieById(@Path("movie_id") id: Int): Observable<MovieDetails>

}