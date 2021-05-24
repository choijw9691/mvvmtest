package com.didimstory.mvvmtest.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.didimstory.mvvmtest.R
import com.didimstory.mvvmtest.data.api.POSTER_BASE_URL
import com.didimstory.mvvmtest.data.api.TheMovieDBClient
import com.didimstory.mvvmtest.data.api.TheMovieDBInterface
import com.didimstory.mvvmtest.data.repository.NetworkState
import com.didimstory.mvvmtest.data.vo.MovieDetails
import kotlinx.android.synthetic.main.activity_single_movie.*


class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)


        val movieId = intent.getIntExtra("id",1)
        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        //뷰 모델 얻기!
        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE

            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })


    }


    fun bindUI(it: MovieDetails) {
Log.d("알고",it.toString())
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.voteCount.toString()
        movie_runtime.text = it.runtime.toString() + " minultes"
        movie_budget.text = it.budget.toString()
        movie_revenue.text = it.revenue.toString()


        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this).load(moviePosterURL).into(iv_movie_poster)
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {

        return ViewModelProvider(this, object : ViewModelProvider.Factory {

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository, movieId) as T
            }

        })[SingleMovieViewModel::class.java]

    }
}