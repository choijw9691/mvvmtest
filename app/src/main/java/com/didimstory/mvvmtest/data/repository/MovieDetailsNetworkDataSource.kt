package com.didimstory.mvvmtest.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.didimstory.mvvmtest.data.api.TheMovieDBInterface
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

//CompositeDisposable : disposable 전부 삭제 가능
class MovieDetailsNetworkDataSource(private val apiService: TheMovieDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadedMovieDetailsResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movidId: Int) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(apiService.getMovieDetails(movidId).subscribeOn(Schedulers.io()).subscribe({

                _downloadedMovieDetailsResponse.postValue(it)
                _networkState.postValue(NetworkState.LOADED)

            }, {

                _networkState.postValue(NetworkState.ERROR)

            }))
        } catch (e: Exception) {
        }


    }


}