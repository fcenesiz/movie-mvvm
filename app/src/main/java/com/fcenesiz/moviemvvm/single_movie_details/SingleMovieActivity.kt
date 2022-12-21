package com.fcenesiz.moviemvvm.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fcenesiz.moviemvvm.databinding.ActivitySingleMovieBinding

class SingleMovieActivity : AppCompatActivity() {
    lateinit var binding: ActivitySingleMovieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}