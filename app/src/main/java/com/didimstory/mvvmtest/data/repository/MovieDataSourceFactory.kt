package com.didimstory.mvvmtest.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.didimstory.mvvmtest.data.api.TheMovieDBInterface
import com.didimstory.mvvmtest.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable


//DataSource.Factory : DataSource를 생성하는 역할
class MovieDataSourceFactory(private val apiService:TheMovieDBInterface,private val compositeDisposable: CompositeDisposable):DataSource.Factory<Int,Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {

val movieDataSource = MovieDataSource(apiService,compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)
return movieDataSource
    }


}