package com.didimstory.mvvmtest.single_movie_details

import androidx.lifecycle.LiveData
import com.didimstory.mvvmtest.data.api.TheMovieDBInterface
import com.didimstory.mvvmtest.data.repository.MovieDetailsNetworkDataSource
import com.didimstory.mvvmtest.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService: TheMovieDBInterface){
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable,movieId:Int):LiveData<MovieDetails>{

        movieDetailsNetworkDataSource= MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)
        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse

    }
    fun getMovieDetailsNetworkState():LiveData<NetworkState>{

        return movieDetailsNetworkDataSource.networkState

    }
}