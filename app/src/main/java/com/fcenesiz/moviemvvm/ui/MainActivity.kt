package com.fcenesiz.moviemvvm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fcenesiz.moviemvvm.databinding.ActivityMainBinding
import com.fcenesiz.moviemvvm.ui.single_movie_details.SingleMovieActivity

// api_key: 398ea00d8c7f65d3aff1699dac3ce42f
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStartSingleMovieActivity.setOnClickListener {
            startActivity(Intent(this, SingleMovieActivity::class.java).apply {
                putExtra("id", 299534)
            })
        }
    }
}