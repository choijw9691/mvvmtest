package com.didimstory.mvvmtest.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.didimstory.mvvmtest.data.repository.NetworkState
import com.didimstory.mvvmtest.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel (private val movieRepository : MovieDetailsRepository,movieId:Int):ViewModel()
{

    private val compositeDisposable=CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> by lazy {

        movieRepository.fetchSingleMovieDetails(compositeDisposable,movieId)

    }

    val networkState : LiveData<NetworkState> by lazy {

       movieRepository.getMovieDetailsNetworkState()

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose() // 메모리 누수 없어짐 !
    }

}