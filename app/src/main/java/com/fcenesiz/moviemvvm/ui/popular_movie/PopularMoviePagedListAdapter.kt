package com.fcenesiz.moviemvvm.ui.popular_movie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fcenesiz.moviemvvm.data.api.POSTER_BASE_URL
import com.fcenesiz.moviemvvm.data.repository.NetworkState
import com.fcenesiz.moviemvvm.data.vo.Movie
import com.fcenesiz.moviemvvm.databinding.MovieItemBinding
import com.fcenesiz.moviemvvm.databinding.NetworkStateItemBinding
import com.fcenesiz.moviemvvm.ui.single_movie_details.SingleMovieActivity


class PopularMoviePagedListAdapter :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(
        MovieDiffCallBack()
    ) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState : NetworkState? = null
    private lateinit var movieItemBinding: MovieItemBinding
    private lateinit var networkStateItemBinding: NetworkStateItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == MOVIE_VIEW_TYPE){
            movieItemBinding = MovieItemBinding.inflate(layoutInflater, parent, false)
            return MovieItemViewHolder(movieItemBinding)
        }else{
            networkStateItemBinding = NetworkStateItemBinding.inflate(layoutInflater, parent, false)
            return NetworkStateItemViewHolder(networkStateItemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position))
        }else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    fun setNetworkState(networkState: NetworkState){
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()

        this.networkState = networkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount())
            }
        }else if (hasExtraRow && previousState != networkState){
            notifyItemChanged(itemCount - 1)
        }
    }

    class MovieItemViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie?) {
            movie?.let {
                binding.apply {
                    textViewTitle.text = it.title
                    textViewReleaseDate.text = it.releaseDate

                    val moviePosterUrl = POSTER_BASE_URL + movie.posterPath
                    Glide.with(this.root)
                        .load(moviePosterUrl)
                        .into(imageView)

                    root.setOnClickListener { view ->
                        val intent = Intent(view.context, SingleMovieActivity::class.java)
                        intent.putExtra("id", it.id)
                        view.context.startActivity(intent)
                    }
                }
            }
        }
    }

    class NetworkStateItemViewHolder(val binding: NetworkStateItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(networkState: NetworkState?) {
            networkState?.let {
                binding.apply {
                    if (networkState == NetworkState.LOADING) {
                        progressBarItem.visibility = View.VISIBLE
                    } else {
                        progressBarItem.visibility = View.GONE
                    }

                    if (networkState == NetworkState.ERROR) {
                        textViewError.visibility = View.VISIBLE
                        textViewError.text = networkState.msg
                    } else if (networkState == NetworkState.END_OF_LIST) {
                        textViewError.visibility = View.VISIBLE
                        textViewError.text = networkState.msg
                    } else {
                        textViewError.visibility = View.GONE
                    }
                }

            }
        }
    }
}